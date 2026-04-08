package com.kym.detector;

import com.kym.entity.StatementCell;
import com.kym.entity.AccountStatementStructure;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AccountStatementStructureDetector {

    public AccountStatementStructure detect(long statementFileId, List<StatementCell> statementCells) {
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::getRowIndex));
        AccountStatementStructure accountStatementStructure = statementCellsByRowIndex.entrySet().stream()
                .filter(l -> {
                            Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                            return cellsByColumnIndex.size() >= 7 &&
                                    "Date".equals(cellsByColumnIndex.get(0).getRawValueText()) &&
                                    "Narration".equals(cellsByColumnIndex.get(1).getRawValueText()) &&
                                    "Chq./Ref.No.".equals(cellsByColumnIndex.get(2).getRawValueText()) &&
                                    "Value Dt".equals(cellsByColumnIndex.get(3).getRawValueText()) &&
                                    "Withdrawal Amt.".equals(cellsByColumnIndex.get(4).getRawValueText()) &&
                                    "Deposit Amt.".equals(cellsByColumnIndex.get(5).getRawValueText()) &&
                                    "Closing Balance".equals(cellsByColumnIndex.get(6).getRawValueText());
                        }
                )
                .map(l -> new AccountStatementStructure(
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

        Integer headerRowIndex = accountStatementStructure.getHeaderRowIndex();
        Integer dateColIndex = accountStatementStructure.getDateColIndex();

        Integer dataStartRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                    StatementCell dateCell = cellsByColumnIndex.get(dateColIndex);
                    return dateCell != null && isValidDate(dateCell.getRawValueText());
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));


        Integer dataEndIndexFound = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, e -> e));
                    StatementCell dateCell = cellsByColumnIndex.get(dateColIndex);
                    return dateCell != null && !isValidDate(dateCell.getRawValueText());
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        Integer dataEndRowIndex = dataEndIndexFound-1;

        accountStatementStructure = new AccountStatementStructure(accountStatementStructure.getStatementFileId(),
                headerRowIndex,
                dateColIndex,
                accountStatementStructure.getNarrationColIndex(),
                accountStatementStructure.getDebitColIndex(),
                accountStatementStructure.getCreditColIndex(),
                accountStatementStructure.getBalanceColIndex(),
                dataStartRowIndex,
                dataEndRowIndex);

        return accountStatementStructure;
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
