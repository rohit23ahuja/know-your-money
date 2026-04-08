package com.kym.repository;

import com.kym.dto.CreditCardTransactionCategorization;

import java.util.List;

public interface CreditCardTransactionJdbcRepository {

    int[] updateTransactionCategorization(List<CreditCardTransactionCategorization> creditCardTransactionCategorizations);
}
