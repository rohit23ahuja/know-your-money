package com.kym.repository;

import com.kym.dto.CreditCardTransactionCategorization;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreditCardTransactionJdbcRepositoryImpl implements CreditCardTransactionJdbcRepository {

    private static final String SQL_UPDATE_CREDIT_CARD_TRANSACTION = """
            update creditcard_transaction set transaction_categorization = ? where statement_file_id = ? and id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    public CreditCardTransactionJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int[] updateTransactionCategorization(List<CreditCardTransactionCategorization> creditCardTransactionCategorizations) {
        List<Object[]> updates = creditCardTransactionCategorizations.stream()
                .map(u -> new Object[]{u.transactionCategorization(), u.statementFileId(), u.transactionId()})
                .toList();
        return jdbcTemplate.batchUpdate(SQL_UPDATE_CREDIT_CARD_TRANSACTION, updates);
    }
}
