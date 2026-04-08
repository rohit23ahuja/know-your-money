package com.kym.service;

import com.kym.dto.CreditCardTransactionCategorization;
import com.kym.entity.CreditCardTransaction;
import com.kym.entity.StatementFile;
import com.kym.repository.CreditCardTransactionJdbcRepository;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCategorizationService {
    private final StatementFileRepository statementFileRepository;
    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final CreditCardTransactionCategorizationService creditCardTransactionCategorizationService;
    private final CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository;

    public TransactionCategorizationService(StatementFileRepository statementFileRepository,
                                            CreditCardTransactionRepository creditCardTransactionRepository,
                                            CreditCardTransactionCategorizationService creditCardTransactionCategorizationService,
                                            CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository) {
        this.statementFileRepository = statementFileRepository;
        this.creditCardTransactionRepository = creditCardTransactionRepository;
        this.creditCardTransactionCategorizationService = creditCardTransactionCategorizationService;
        this.creditCardTransactionJdbcRepository = creditCardTransactionJdbcRepository;
    }

    public int[] categorize(long statementFileId) {
        StatementFile statementFile = statementFileRepository.findById(statementFileId).get();
        if ("credit-card-statement".equals(statementFile.getStatementType())) {
            List<CreditCardTransaction> creditCardTransactions = creditCardTransactionRepository.findByStatementFileId(statementFileId);
            List<CreditCardTransactionCategorization> creditCardTransactionCategorizations = creditCardTransactionCategorizationService.categorize(creditCardTransactions);
            return creditCardTransactionJdbcRepository.updateTransactionCategorization(creditCardTransactionCategorizations);
        } else {
//TODO
        }
        return null;
    }
}
