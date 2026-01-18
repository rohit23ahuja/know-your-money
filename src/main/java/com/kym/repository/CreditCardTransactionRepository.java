package com.kym.repository;

import com.kym.model.CreditCardTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.kym.util.Constants.*;

public class CreditCardTransactionRepository {
    private static final String SQL_INSERT_CREDIT_CARD_TRANSACTION = """
            insert into 
            creditcard_transaction(statement_file_id, txn_type, customer_name, txn_date, txn_time, description, rewards, amt, debit_credit, source_row_index)
            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    public int[] save(List<CreditCardTransaction> creditCardTransactions) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CREDIT_CARD_TRANSACTION)) {
            for (CreditCardTransaction creditCardTransaction : creditCardTransactions) {
                preparedStatement.setLong(1, creditCardTransaction.statementFileId());
                preparedStatement.setString(2, creditCardTransaction.transactionType());
                preparedStatement.setString(3, creditCardTransaction.customerName());
                preparedStatement.setObject(4, creditCardTransaction.transactionDate());
                preparedStatement.setObject(5, creditCardTransaction.transactionTime());
                preparedStatement.setString(6, creditCardTransaction.description());
                preparedStatement.setInt(7, creditCardTransaction.rewards());
                preparedStatement.setBigDecimal(8, creditCardTransaction.amt());
                preparedStatement.setString(9, creditCardTransaction.debitCredit());
                preparedStatement.setInt(10, creditCardTransaction.sourceRowIndex());
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Exception occurred while saving credit card transactions.", e);
        }
    }
}
