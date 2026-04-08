package com.kym.reader;

import com.kym.entity.AccountTransaction;
import com.kym.entity.StatementCell;
import com.kym.entity.AccountStatementStructure;
import com.kym.repository.AccountTransactionRepository;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.AccountStatementStructureRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AccountTransactionReader {
    private static final DateTimeFormatter STATEMENT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final StatementCellRepository statementCellRepository;
    private final AccountStatementStructureRepository accountStatementStructureRepository;
    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionReader(StatementCellRepository statementCellRepository,
                                    AccountStatementStructureRepository accountStatementStructureRepository,
                                    AccountTransactionRepository accountTransactionRepository) {
        this.statementCellRepository = statementCellRepository;
        this.accountStatementStructureRepository = accountStatementStructureRepository;
        this.accountTransactionRepository = accountTransactionRepository;
    }


    public void readTransactions(long statementFileId) {
        AccountStatementStructure accountStatementStructure = accountStatementStructureRepository.findByStatementFileId(statementFileId).get();
        List<StatementCell> statementCells = statementCellRepository.findStatementCellsInRowRange(
                statementFileId,
                accountStatementStructure.getDataStartRowIndex(),
                accountStatementStructure.getDataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::getRowIndex));

        List<AccountTransaction> accountTransactions = new ArrayList<>();
        statementCellsByRowIndex.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(statementCellEntry -> {
                    Map<Integer, StatementCell> cellsByColumnIndex =
                            statementCellEntry.getValue().stream()
                                    .collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                    StatementCell dateCell = cellsByColumnIndex.get(accountStatementStructure.getDateColIndex());
                    StatementCell narrationCell = cellsByColumnIndex.get(accountStatementStructure.getNarrationColIndex());
                    StatementCell debitCell = cellsByColumnIndex.get(accountStatementStructure.getDebitColIndex());
                    StatementCell creditCell = cellsByColumnIndex.get(accountStatementStructure.getCreditColIndex());
                    StatementCell balanceCell = cellsByColumnIndex.get(accountStatementStructure.getBalanceColIndex());

                    if(dateCell == null || narrationCell == null || balanceCell == null) {
                        throw new RuntimeException("Mandatory column missing at row "+statementCellEntry.getKey());
                    }

                    LocalDate txnDate;
                    try {
                        txnDate = LocalDate.parse(dateCell.getRawValueText(), STATEMENT_DATE_FORMAT);
                    } catch (DateTimeParseException e) {
                        throw new RuntimeException(
                                "Invalid date format: " + dateCell.getRawValueText() + ", expected dd/MM/uu", e
                        );
                    }
                    accountTransactions.add(
                            new AccountTransaction(
                                    statementFileId,
                                    txnDate,
                                    narrationCell.getRawValueText(),
                                    parseAmount(debitCell!=null?debitCell.getRawValueText():null),
                                    parseAmount(creditCell!=null?creditCell.getRawValueText():null),
                                    parseAmount(balanceCell.getRawValueText()),
                                    statementCellEntry.getKey()));
                });
        accountTransactionRepository.saveAll(accountTransactions);
    }

    private static BigDecimal parseAmount(String text) {
        if(text ==null || text.isBlank()) return null;
        return new BigDecimal(text.replace(",", ""));
    }

}
