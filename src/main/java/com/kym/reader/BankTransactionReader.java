package com.kym.reader;

import com.kym.model.BankTransaction;
import com.kym.model.StatementCell;
import com.kym.model.StatementStructure;
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
        StatementStructure statementStructure = statementStructureWriter.getStatementStructure(statementFileId);
        List<StatementCell> statementCells = statementCellWriter.getStatementCells(statementFileId,
                statementStructure.dataStartRowIndex(), statementStructure.dataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));

        List<BankTransaction> bankTransactions = new ArrayList<>();
        for (Map.Entry<Integer, List<StatementCell>> statementCellEntry : statementCellsByRowIndex.entrySet()) {
            LocalDate txnDate;
            try {
                txnDate = LocalDate.parse(statementCellEntry.getValue().get(statementStructure.dateColIndex()).rawValueText(), STATEMENT_DATE_FORMAT);
            } catch (DateTimeParseException e) {
                throw new RuntimeException(
                        "Invalid date format: " + statementCellEntry.getValue().get(statementStructure.dateColIndex()).rawValueText() + ", expected dd/MM/uu", e
                );
            }
            bankTransactions.add(
                    new BankTransaction(
                            statementFileId,
                            txnDate,
                            statementCellEntry.getValue().get(statementStructure.narrationColIndex()).rawValueText(),
                            statementCellEntry.getValue().get(statementStructure.debitColIndex()).rawValueText() != null &&
                                    !("".equals(statementCellEntry.getValue().get(statementStructure.debitColIndex()).rawValueText())) ?
                            new BigDecimal(statementCellEntry.getValue().get(statementStructure.debitColIndex()).rawValueText()) : null,
                            statementCellEntry.getValue().get(statementStructure.creditColIndex()).rawValueText() != null &&
                                    !("".equals(statementCellEntry.getValue().get(statementStructure.creditColIndex()).rawValueText())) ?
                            new BigDecimal(statementCellEntry.getValue().get(statementStructure.creditColIndex()).rawValueText()) : null,
                            new BigDecimal(statementCellEntry.getValue().get(statementStructure.balanceColIndex()).rawValueText()),
                            statementCellEntry.getKey()));
        }
        bankTransactionRepository.save(bankTransactions);
    }


}
