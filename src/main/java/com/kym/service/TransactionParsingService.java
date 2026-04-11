package com.kym.service;

import com.kym.entity.StatementDetail;
import com.kym.entity.StatementFile;
import com.kym.reader.AccountTransactionReader;
import com.kym.reader.CreditCardTransactionReader;
import com.kym.repository.StatementDetailRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionParsingService {

    private final StatementDetailRepository statementDetailRepository;
    private final CreditCardTransactionReader creditCardTransactionReader;
    private final AccountTransactionReader accountTransactionReader;

    public TransactionParsingService(StatementDetailRepository statementDetailRepository,
                                     CreditCardTransactionReader creditCardTransactionReader,
                                     AccountTransactionReader accountTransactionReader) {
        this.statementDetailRepository = statementDetailRepository;
        this.creditCardTransactionReader = creditCardTransactionReader;
        this.accountTransactionReader = accountTransactionReader;
    }

    public Integer readTransactions(long statementFileId) {
        StatementDetail statementDetail = statementDetailRepository.findByStatementFileId(statementFileId);
        Integer parsedTransactionsCount = null;
        if("credit-card-statement".equals(statementDetail.getStatementType())) {
            parsedTransactionsCount = creditCardTransactionReader.parseAndSaveTransactions(statementFileId).size();
        } else {
            parsedTransactionsCount = accountTransactionReader.parseAndSaveTransactions(statementFileId).size();
        }
        return parsedTransactionsCount;
    }
}
