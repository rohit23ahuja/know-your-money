package com.kym.detector;

import com.kym.entity.CreditCardStatementStructure;
import com.kym.entity.StatementCell;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CreditCardStatementStructureDetector {

    public CreditCardStatementStructure detect(long statementFileId, List<StatementCell> statementCells) {
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::getRowIndex));
        CreditCardStatementStructure creditCardStatementStructure = statementCellsByRowIndex.entrySet().stream()
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                    return cellsByColumnIndex.size() >= 25 &&
                            "Transaction type".equals(cellsByColumnIndex.get(0).getRawValueText()) &&
                            "Primary / Addon Customer Name".equals(cellsByColumnIndex.get(4).getRawValueText()) &&
                            "Date & Time".equals(cellsByColumnIndex.get(9).getRawValueText()) &&
                            "Description".equals(cellsByColumnIndex.get(12).getRawValueText()) &&
                            "REWARDS".equals(cellsByColumnIndex.get(18).getRawValueText()) &&
                            "AMT".equals(cellsByColumnIndex.get(20).getRawValueText()) &&
                            "Debit / Credit".equals(cellsByColumnIndex.get(23).getRawValueText());
                })
                .map(l -> new CreditCardStatementStructure(
                        statementFileId,
                        l.getKey(),
                        0,
                        4,
                        9,
                        12,
                        18,
                        20, 23, 0, 0))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to detect header row."));
        Integer headerRowIndex = creditCardStatementStructure.getHeaderRowIndex();
        Integer transactionTypeColIndex = creditCardStatementStructure.getTransactiontypeColIndex();

        Integer dataStartRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                    StatementCell transactionTypeCell = cellsByColumnIndex.get(transactionTypeColIndex);
                    return transactionTypeCell != null &&
                            ("Domestic".equals(transactionTypeCell.getRawValueText()) ||
                                    "International".equals(transactionTypeCell.getRawValueText()));
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));

        Integer dataEndIndexFound = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::getColIndex, e -> e));
                    StatementCell transactionTypeCell = cellsByColumnIndex.get(transactionTypeColIndex);
                    return transactionTypeCell != null &&
                            !("Domestic".equals(transactionTypeCell.getRawValueText()) ||
                                    "International".equals(transactionTypeCell.getRawValueText()));
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        Integer dataEndRowIndex = dataEndIndexFound - 1;

        return new CreditCardStatementStructure(
                creditCardStatementStructure.getStatementFileId(),
                headerRowIndex,
                transactionTypeColIndex,
                creditCardStatementStructure.getCustomernameColIndex(),
                creditCardStatementStructure.getDatetimeColIndex(),
                creditCardStatementStructure.getDescriptionColIndex(),
                creditCardStatementStructure.getRewardsColIndex(),
                creditCardStatementStructure.getAmtColIndex(),
                creditCardStatementStructure.getDebitcreditColIndex(),
                dataStartRowIndex, dataEndRowIndex);
    }
}
