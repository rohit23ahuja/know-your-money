package com.kym.reader;

import com.kym.model.AccountTransaction;
import com.kym.model.StatementCell;
import com.kym.model.AccountStatementStructure;
import com.kym.repository.AccountTransactionRepository;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.AccountStatementStructureWriter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountTransactionReader {
    private static final DateTimeFormatter STATEMENT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final StatementCellWriter statementCellWriter;
    private final AccountStatementStructureWriter accountStatementStructureWriter;
    private final AccountTransactionRepository accountTransactionRepository;
    private final Long statementFileId;

    public AccountTransactionReader(long statementFileId) {
        this.statementFileId = statementFileId;
        statementCellWriter = new StatementCellWriter(statementFileId);
        accountStatementStructureWriter = new AccountStatementStructureWriter(statementFileId);
        accountTransactionRepository = new AccountTransactionRepository();
    }


    public void readTransactions() {
        AccountStatementStructure accountStatementStructure = accountStatementStructureWriter.getStatementStructure();
        List<StatementCell> statementCells = statementCellWriter.getStatementCells(
                accountStatementStructure.dataStartRowIndex(),
                accountStatementStructure.dataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));

        List<AccountTransaction> accountTransactions = new ArrayList<>();
        statementCellsByRowIndex.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(statementCellEntry -> {
                    Map<Integer, StatementCell> cellsByColumnIndex =
                            statementCellEntry.getValue().stream()
                                    .collect(Collectors.toMap(StatementCell::columnIndex, c -> c));
                    StatementCell dateCell = cellsByColumnIndex.get(accountStatementStructure.dateColIndex());
                    StatementCell narrationCell = cellsByColumnIndex.get(accountStatementStructure.narrationColIndex());
                    StatementCell debitCell = cellsByColumnIndex.get(accountStatementStructure.debitColIndex());
                    StatementCell creditCell = cellsByColumnIndex.get(accountStatementStructure.creditColIndex());
                    StatementCell balanceCell = cellsByColumnIndex.get(accountStatementStructure.balanceColIndex());

                    if(dateCell == null || narrationCell == null || balanceCell == null) {
                        throw new RuntimeException("Mandatory column missing at row "+statementCellEntry.getKey());
                    }

                    LocalDate txnDate;
                    try {
                        txnDate = LocalDate.parse(dateCell.rawValueText(), STATEMENT_DATE_FORMAT);
                    } catch (DateTimeParseException e) {
                        throw new RuntimeException(
                                "Invalid date format: " + dateCell.rawValueText() + ", expected dd/MM/uu", e
                        );
                    }
                    accountTransactions.add(
                            new AccountTransaction(
                                    statementFileId,
                                    txnDate,
                                    narrationCell.rawValueText(),
                                    parseAmount(debitCell!=null?debitCell.rawValueText():null),
                                    parseAmount(creditCell!=null?creditCell.rawValueText():null),
                                    parseAmount(balanceCell.rawValueText()),
                                    statementCellEntry.getKey()));
                });
        accountTransactionRepository.save(accountTransactions);
    }

    private static BigDecimal parseAmount(String text) {
        if(text ==null || text.isBlank()) return null;
        return new BigDecimal(text.replace(",", ""));
    }

}
