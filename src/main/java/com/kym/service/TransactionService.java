package com.kym.service;

import com.kym.entity.StatementFile;
import com.kym.reader.AccountTransactionReader;
import com.kym.reader.CreditCardTransactionReader;
import com.kym.repository.StatementFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private StatementFileRepository statementFileRepository;

    @Autowired
    private CreditCardTransactionReader creditCardTransactionReader;

    @Autowired
    private AccountTransactionReader accountTransactionReader;

    public void readTransactions(long statementFileId) {
        StatementFile statementFile = statementFileRepository.findById(statementFileId).get();

        if("credit-card-statement".equals(statementFile.getStatementType())) {
            creditCardTransactionReader.readTransactions(statementFileId);
        } else {
            accountTransactionReader.readTransactions(statementFileId);
        }
    }
}
