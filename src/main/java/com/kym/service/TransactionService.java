package com.kym.service;

import com.kym.model.StatementFile;
import com.kym.reader.AccountTransactionReader;
import com.kym.reader.CreditCardTransactionReader;
import com.kym.writer.StatementFileWriter;

public class TransactionService {
    private final Long statementFileId;
    private final StatementFileWriter statementFileWriter;

    public TransactionService(Long statementFileId) {
        this.statementFileId = statementFileId;
        statementFileWriter = new StatementFileWriter();
    }


    public void readTransactions() {
        StatementFile statementFile = statementFileWriter.getStatementFile(statementFileId);

        if("credit-card-statement".equals(statementFile.statementType())) {
            CreditCardTransactionReader creditCardTransactionReader = new CreditCardTransactionReader(statementFileId);
            creditCardTransactionReader.readTransactions();
        } else {
            AccountTransactionReader accountTransactionReader = new AccountTransactionReader(statementFileId);
            accountTransactionReader.readTransactions();
        }
    }
}
