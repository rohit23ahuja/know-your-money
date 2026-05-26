package com.kym.service;

import com.kym.dto.CreditCardTransactionCategorization;
import com.kym.entity.CreditCardTransaction;
import com.kym.entity.StatementDetail;
import com.kym.entity.StatementFile;
import com.kym.entity.Transaction;
import com.kym.repository.CreditCardTransactionJdbcRepository;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.repository.StatementDetailRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionCategorizationService<T> {

    int[] categorize(List<T> transactions, StatementDetail statementDetail);
}
