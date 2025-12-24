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
                .filter(l ->
                        l.getValue().size() >= 7 &&
                                "Date".equals(l.getValue().get(0).rawValueText()) &&
                                "Narration".equals(l.getValue().get(1).rawValueText()) &&
                                "Chq./Ref.No.".equals(l.getValue().get(2).rawValueText()) &&
                                "Value Dt".equals(l.getValue().get(3).rawValueText()) &&
                                "Withdrawal Amt.".equals(l.getValue().get(4).rawValueText()) &&
                                "Deposit Amt.".equals(l.getValue().get(5).rawValueText()) &&
                                "Closing Balance".equals(l.getValue().get(6).rawValueText())
                )
                .map(l -> new StatementStructure(
                        statementFileId,
                        l.getKey(),
                        l.getValue().get(0).columnIndex(),
                        l.getValue().get(1).columnIndex(),
                        l.getValue().get(4).columnIndex(),
                        l.getValue().get(5).columnIndex(),
                        l.getValue().get(6).columnIndex(),
                        0, 0))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to detect header row."));

        Integer headerRowIndex = statementStructure.headerRowIndex();
        Integer dateColIndex = statementStructure.dateColIndex();

        Integer dataStartRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l ->
                        isValidDate(l.getValue().get(dateColIndex).rawValueText()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));


        Integer dataEndRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l ->
                        !isValidDate(l.getValue().get(dateColIndex).rawValueText()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        --dataEndRowIndex;

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
