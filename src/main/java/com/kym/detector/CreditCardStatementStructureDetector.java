package com.kym.detector;

import com.kym.model.AccountStatementStructure;
import com.kym.model.CreditCardStatementStructure;
import com.kym.model.StatementCell;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreditCardStatementStructureDetector {
    private final long statementFileId;

    public CreditCardStatementStructureDetector(long statementFileId) {
        this.statementFileId = statementFileId;
    }


    public CreditCardStatementStructure detect(List<StatementCell> statementCells) {
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));
        CreditCardStatementStructure creditCardStatementStructure = statementCellsByRowIndex.entrySet().stream()
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                    return cellsByColumnIndex.size() >= 25 &&
                            "Transaction type".equals(cellsByColumnIndex.get(0).rawValueText()) &&
                            "Primary / Addon Customer Name".equals(cellsByColumnIndex.get(4).rawValueText()) &&
                            "Date & Time".equals(cellsByColumnIndex.get(9).rawValueText()) &&
                            "Description".equals(cellsByColumnIndex.get(12).rawValueText()) &&
                            "REWARDS".equals(cellsByColumnIndex.get(18).rawValueText()) &&
                            "AMT".equals(cellsByColumnIndex.get(20).rawValueText()) &&
                            "Debit / Credit".equals(cellsByColumnIndex.get(23).rawValueText());
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
        Integer headerRowIndex = creditCardStatementStructure.headerRowIndex();
        Integer transactionTypeColIndex = creditCardStatementStructure.transactionTypeColIndex();

        Integer dataStartRowIndex = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                    StatementCell transactionTypeCell = cellsByColumnIndex.get(transactionTypeColIndex);
                    return transactionTypeCell != null &&
                            ("Domestic".equals(transactionTypeCell.rawValueText()) ||
                                    "International".equals(transactionTypeCell.rawValueText()));
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));

        Integer dataEndIndexFound = statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l -> {
                    Map<Integer, StatementCell> cellsByColumnIndex = l.getValue().stream().collect(Collectors.toMap(StatementCell::columnIndex, e -> e));
                    StatementCell transactionTypeCell = cellsByColumnIndex.get(transactionTypeColIndex);
                    return transactionTypeCell != null &&
                            !("Domestic".equals(transactionTypeCell.rawValueText()) ||
                                    "International".equals(transactionTypeCell.rawValueText()));
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        Integer dataEndRowIndex = dataEndIndexFound-1;


        return new CreditCardStatementStructure(creditCardStatementStructure.statementFileId(), headerRowIndex, transactionTypeColIndex, creditCardStatementStructure.customerNameColIndex(), creditCardStatementStructure.dateTimeColIndex(), creditCardStatementStructure.descriptionColIndex(), creditCardStatementStructure.rewardsColIndex(), creditCardStatementStructure.amtColIndex(), creditCardStatementStructure.debitCreditColIndex(), dataStartRowIndex, dataEndRowIndex);
    }
}
