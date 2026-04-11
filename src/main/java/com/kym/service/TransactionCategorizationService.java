package com.kym.service;

import com.kym.dto.CreditCardTransactionCategorization;
import com.kym.entity.CreditCardTransaction;
import com.kym.entity.StatementDetail;
import com.kym.entity.StatementFile;
import com.kym.repository.CreditCardTransactionJdbcRepository;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.repository.StatementDetailRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCategorizationService {
    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final CreditCardTransactionCategorizationService creditCardTransactionCategorizationService;
    private final CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository;
    private final StatementDetailRepository statementDetailRepository;

    public TransactionCategorizationService(CreditCardTransactionRepository creditCardTransactionRepository,
                                            CreditCardTransactionCategorizationService creditCardTransactionCategorizationService,
                                            CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository,
                                            StatementDetailRepository statementDetailRepository) {
        this.creditCardTransactionRepository = creditCardTransactionRepository;
        this.creditCardTransactionCategorizationService = creditCardTransactionCategorizationService;
        this.creditCardTransactionJdbcRepository = creditCardTransactionJdbcRepository;
        this.statementDetailRepository = statementDetailRepository;
    }

    public int[] categorize(long statementFileId) {
        StatementDetail statementDetail = statementDetailRepository.findByStatementFileId(statementFileId);
        if ("credit-card-statement".equals(statementDetail.getStatementType())) {
            List<CreditCardTransaction> creditCardTransactions = creditCardTransactionRepository.findByStatementFileId(statementFileId);
            List<CreditCardTransactionCategorization> creditCardTransactionCategorizations = creditCardTransactionCategorizationService.categorize(creditCardTransactions);
            return creditCardTransactionJdbcRepository.updateTransactionCategorization(creditCardTransactionCategorizations);
        } else {
//TODO
        }
        return null;
    }
}
