package com.kym.repository;

import com.kym.model.CreditCardTransactionCategorization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.kym.util.Constants.*;

public class CreditCardTransactionCategorizationRepository {
    private static final String SQL_INSERT_CREDITCARD_TRANSACTION_CATEGORIZATION = """
            insert into creditcard_transaction_categorization(statement_file_id, transaction_id, transaction_categorization)\s
            values(?, ?, ?)
            """;

    public int[] save(List<CreditCardTransactionCategorization> creditCardTransactionCategorizations) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CREDITCARD_TRANSACTION_CATEGORIZATION)) {
            for (CreditCardTransactionCategorization creditCardTransactionCategorization : creditCardTransactionCategorizations) {
                preparedStatement.setLong(1, creditCardTransactionCategorization.statementFileId());
                preparedStatement.setLong(2, creditCardTransactionCategorization.transactionId());
                preparedStatement.setString(3, creditCardTransactionCategorization.transactionCategorization());
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Exception occurred while saving credit card transaction categorization", e);
        }
    }
}
