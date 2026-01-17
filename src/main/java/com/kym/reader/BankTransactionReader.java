package com.kym.reader;

import com.kym.model.BankTransaction;
import com.kym.model.StatementCell;
import com.kym.model.AccountStatementStructure;
import com.kym.repository.BankTransactionRepository;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementStructureWriter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankTransactionReader {
    private static final DateTimeFormatter STATEMENT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final StatementCellWriter statementCellWriter;
    private final StatementStructureWriter statementStructureWriter;
    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionReader() {
        statementCellWriter = new StatementCellWriter();
        statementStructureWriter = new StatementStructureWriter();
        bankTransactionRepository = new BankTransactionRepository();
    }


    public void readTransactions(long statementFileId) {
        AccountStatementStructure accountStatementStructure = statementStructureWriter.getStatementStructure(statementFileId);
        List<StatementCell> statementCells = statementCellWriter.getStatementCells(statementFileId,
                accountStatementStructure.dataStartRowIndex(), accountStatementStructure.dataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));

        List<BankTransaction> bankTransactions = new ArrayList<>();
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
                    bankTransactions.add(
                            new BankTransaction(
                                    statementFileId,
                                    txnDate,
                                    narrationCell.rawValueText(),
                                    parseAmount(debitCell!=null?debitCell.rawValueText():null),
                                    parseAmount(creditCell!=null?creditCell.rawValueText():null),
                                    parseAmount(balanceCell.rawValueText()),
                                    statementCellEntry.getKey()));
                });
        bankTransactionRepository.save(bankTransactions);
    }

    private static BigDecimal parseAmount(String text) {
        if(text ==null || text.isBlank()) return null;
        return new BigDecimal(text.replace(",", ""));
    }

}
