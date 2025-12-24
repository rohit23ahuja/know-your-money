package com.kym.detector;

import com.kym.model.StatementCell;
import com.kym.model.StatementStructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementStructureDetector {


    public StatementStructure detect(long statementFileId, List<StatementCell> statementCells) {
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));
        StatementStructure statementStructure = statementCellsByRowIndex.entrySet().stream()
                .filter(l -> {
                            Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                            return cellsByColumnIndex.size() >= 7 &&
                                    "Date".equals(cellsByColumnIndex.get(0).rawValueText()) &&
                                    "Narration".equals(cellsByColumnIndex.get(1).rawValueText()) &&
                                    "Chq./Ref.No.".equals(cellsByColumnIndex.get(2).rawValueText()) &&
                                    "Value Dt".equals(cellsByColumnIndex.get(3).rawValueText()) &&
                                    "Withdrawal Amt.".equals(cellsByColumnIndex.get(4).rawValueText()) &&
                                    "Deposit Amt.".equals(cellsByColumnIndex.get(5).rawValueText()) &&
                                    "Closing Balance".equals(cellsByColumnIndex.get(6).rawValueText());
                        }
                )
                .map(l -> new StatementStructure(
                        statementFileId,
                        l.getKey(),
                        0,
                        1,
                        4,
                        5,
                        6,
                        0, 0))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to detect header row."));

        Integer headerRowIndex = statementStructure.headerRowIndex();
        Integer dateColIndex = statementStructure.dateColIndex();

        Integer dataStartRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                    StatementCell dateCell = cellsByColumnIndex.get(dateColIndex);
                    return dateCell != null && isValidDate(dateCell.rawValueText());
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));


        Integer dataEndIndexFound = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, e -> e));
                    StatementCell dateCell = cellsByColumnIndex.get(dateColIndex);
                    return dateCell != null && !isValidDate(dateCell.rawValueText());
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        Integer dataEndRowIndex = dataEndIndexFound-1;

        statementStructure = new StatementStructure(statementStructure.statementFileId(),
                headerRowIndex,
                dateColIndex,
                statementStructure.narrationColIndex(),
                statementStructure.debitColIndex(),
                statementStructure.creditColIndex(),
                statementStructure.balanceColIndex(),
                dataStartRowIndex,
                dataEndRowIndex);

        return statementStructure;
    }


    public static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/uu")
                        .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(dateStr, formatter);
            return true;   // valid date in expected format
        } catch (DateTimeParseException e) {
            return false;  // invalid or wrong format
        }
    }

}
