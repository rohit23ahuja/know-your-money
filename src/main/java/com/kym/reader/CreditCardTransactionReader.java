package com.kym.reader;

import com.kym.entity.CreditCardTransaction;
import com.kym.entity.CreditCardStatementStructure;
import com.kym.entity.StatementCell;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.repository.CreditCardStatementStructureRepository;
import com.kym.repository.StatementCellRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CreditCardTransactionReader {

    private final StatementCellRepository statementCellRepository;
    private final CreditCardStatementStructureRepository creditCardStatementStructureRepository;
    private final CreditCardTransactionRepository creditCardTransactionRepository;

    public CreditCardTransactionReader(StatementCellRepository statementCellRepository,
                                       CreditCardStatementStructureRepository creditCardStatementStructureRepository,
                                       CreditCardTransactionRepository creditCardTransactionRepository) {
        this.statementCellRepository = statementCellRepository;
        this.creditCardStatementStructureRepository = creditCardStatementStructureRepository;
        this.creditCardTransactionRepository = creditCardTransactionRepository;
    }

    private static BigDecimal parseAmount(String text) {
        if (text == null || text.isBlank()) return null;
        return new BigDecimal(text.replace(",", ""));
    }

    public List<CreditCardTransaction> parseAndSaveTransactions(long statementFileId) {
        CreditCardStatementStructure creditCardStatementStructure = creditCardStatementStructureRepository.findByStatementFileId(statementFileId);
        List<StatementCell> statementCells = statementCellRepository.findStatementCellsInRowRange(
                statementFileId,
                creditCardStatementStructure.getDataStartRowIndex(),
                creditCardStatementStructure.getDataEndRowIndex());
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::getRowIndex));

        List<CreditCardTransaction> creditCardTransactions = new ArrayList<>();
        statementCellsByRowIndex.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(statementCellEntry -> {
                    Map<Integer, StatementCell> statementCellsByColIndex =
                            statementCellEntry.getValue().stream()
                                    .collect(Collectors.toMap(StatementCell::getColIndex, c -> c));
                    StatementCell transactionTypeCell = statementCellsByColIndex.get(creditCardStatementStructure.getTransactiontypeColIndex());
                    StatementCell customerNameCell = statementCellsByColIndex.get(creditCardStatementStructure.getCustomernameColIndex());

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy / HH:mm");
                    LocalDateTime parsedLocalDateTime = LocalDateTime.parse(statementCellsByColIndex.get(
                                    creditCardStatementStructure.getDatetimeColIndex()).getRawValueText(),
                            dateTimeFormatter);

                    StatementCell descriptionCell = statementCellsByColIndex.get(creditCardStatementStructure.getDescriptionColIndex());
                    StatementCell rewardsCell = statementCellsByColIndex.get(creditCardStatementStructure.getRewardsColIndex());
                    StatementCell amtCell = statementCellsByColIndex.get(creditCardStatementStructure.getAmtColIndex());
                    StatementCell debitCreditCell = statementCellsByColIndex.get(creditCardStatementStructure.getDebitcreditColIndex());

                    creditCardTransactions.add(
                            new CreditCardTransaction(
                                    statementFileId,
                                    transactionTypeCell.getRawValueText(),
                                    customerNameCell.getRawValueText(),
                                    parsedLocalDateTime,
                                    descriptionCell.getRawValueText(),
                                    !rewardsCell.getRawValueText().isBlank() ?
                                            Integer.parseInt(
                                                    rewardsCell.getRawValueText().replace(" ", "")) : 0,
                                    amtCell != null ? parseAmount(amtCell.getRawValueText()) : null,
                                    debitCreditCell.getRawValueText(),
                                    statementCellEntry.getKey()));
                });
        return creditCardTransactionRepository.saveAll(creditCardTransactions);

    }
}
