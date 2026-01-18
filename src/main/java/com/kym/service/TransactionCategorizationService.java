package com.kym.service;

import com.kym.model.CreditCardTransaction;
import com.kym.model.StatementFile;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.writer.StatementFileWriter;

import java.util.List;

public class TransactionCategorizationService {
    private final long statementFileId;
    private final StatementFileWriter statementFileWriter;
    private final CreditCardTransactionRepository creditCardTransactionRepository;


    public TransactionCategorizationService(long statementFileId) {
        this.statementFileId = statementFileId;
        statementFileWriter = new StatementFileWriter();
        creditCardTransactionRepository = new CreditCardTransactionRepository(statementFileId);
    }

    public void categorize() {
        StatementFile statementFile = statementFileWriter.getStatementFile(statementFileId);
        if ("credit-card-statement".equals(statementFile.statementType())) {
            List<CreditCardTransaction> creditCardTransactions = creditCardTransactionRepository.getCreditCardTransactions();
            CreditCardTransactionCategorizationService creditCardTransactionCategorizationService = new CreditCardTransactionCategorizationService();
            creditCardTransactionCategorizationService.categorize(creditCardTransactions);
        } else {

        }
    }
}
