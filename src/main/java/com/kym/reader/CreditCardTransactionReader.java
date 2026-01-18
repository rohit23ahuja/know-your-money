package com.kym.reader;

import com.kym.model.CreditCardStatementStructure;
import com.kym.model.CreditCardTransaction;
import com.kym.model.StatementCell;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.writer.CreditCardStatementStructureWriter;
import com.kym.writer.StatementCellWriter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreditCardTransactionReader {
    private final StatementCellWriter statementCellWriter;
    private final CreditCardStatementStructureWriter creditCardStatementStructureWriter;
    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final Long statementFileId;

    public CreditCardTransactionReader(long statementFileId) {
        this.statementFileId = statementFileId;
        this.statementCellWriter = new StatementCellWriter(statementFileId);
        this.creditCardStatementStructureWriter = new CreditCardStatementStructureWriter(statementFileId);
        this.creditCardTransactionRepository = new CreditCardTransactionRepository();
    }

    private static BigDecimal parseAmount(String text) {
        if (text == null || text.isBlank()) return null;
        return new BigDecimal(text.replace(",", ""));
    }

    public void readTransactions() {
        CreditCardStatementStructure creditCardStatementStructure = creditCardStatementStructureWriter.getCreditCardStatementStructure();
        List<StatementCell> statementCells = statementCellWriter.getStatementCells(
                creditCardStatementStructure.dataStartRowIndex(),
                creditCardStatementStructure.dataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));

        List<CreditCardTransaction> creditCardTransactions = new ArrayList<>();
        statementCellsByRowIndex.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(statementCellEntry -> {
                    Map<Integer, StatementCell> statementCellsByColIndex =
                            statementCellEntry.getValue().stream()
                                    .collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                    StatementCell transactionTypeCell = statementCellsByColIndex.get(creditCardStatementStructure.transactionTypeColIndex());
                    StatementCell customerNameCell = statementCellsByColIndex.get(creditCardStatementStructure.customerNameColIndex());

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy / HH:mm");
                    LocalDateTime parsedLocalDateTime = LocalDateTime.parse(statementCellsByColIndex.get(
                                    creditCardStatementStructure.dateTimeColIndex()).rawValueText(),
                            dateTimeFormatter);
                    LocalDate transactionDate = parsedLocalDateTime.toLocalDate();
                    LocalTime transactionTime = parsedLocalDateTime.toLocalTime();

                    StatementCell descriptionCell = statementCellsByColIndex.get(creditCardStatementStructure.descriptionColIndex());
                    StatementCell rewardsCell = statementCellsByColIndex.get(creditCardStatementStructure.rewardsColIndex());
                    StatementCell amtCell = statementCellsByColIndex.get(creditCardStatementStructure.amtColIndex());
                    StatementCell debitCreditCell = statementCellsByColIndex.get(creditCardStatementStructure.debitCreditColIndex());

                    creditCardTransactions.add(
                            new CreditCardTransaction(
                                    statementFileId,
                                    transactionTypeCell.rawValueText(),
                                    customerNameCell.rawValueText(),
                                    transactionDate,
                                    transactionTime,
                                    descriptionCell.rawValueText(),
                                    !rewardsCell.rawValueText().isBlank() ?
                                            Integer.parseInt(
                                                    rewardsCell.rawValueText().replace(" ", "")) : 0,
                                    amtCell != null ? parseAmount(amtCell.rawValueText()) : null,
                                    debitCreditCell.rawValueText(),
                                    statementCellEntry.getKey()));
                });
        creditCardTransactionRepository.save(creditCardTransactions);

    }
}
