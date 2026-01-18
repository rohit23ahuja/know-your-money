package com.kym.repository;

import com.kym.model.CreditCardTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.kym.util.Constants.*;

public class CreditCardTransactionRepository {
    private static final String SQL_INSERT_CREDIT_CARD_TRANSACTION = """
            insert into 
            creditcard_transaction(statement_file_id, txn_type, customer_name, txn_date, txn_time, description, rewards, amt, debit_credit, source_row_index)
            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String SQL_SELECT_CREDIT_CARD_TRANSACATION = """
            select 
            statement_file_id, txn_type, customer_name, txn_date, txn_time, description, rewards, amt, debit_credit, source_row_index, id 
            from creditcard_transaction where statement_file_id = ? order by id;
            """;

    private final long statementFileId;

    public CreditCardTransactionRepository(long statementFileId) {
        this.statementFileId = statementFileId;
    }

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

    public List<CreditCardTransaction> getCreditCardTransactions() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_CREDIT_CARD_TRANSACATION)) {
            List<CreditCardTransaction> creditCardTransactions = new ArrayList<>();
            preparedStatement.setLong(1, statementFileId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    creditCardTransactions.add(new CreditCardTransaction(
                            statementFileId,
                            resultSet.getString("txn_type"),
                            resultSet.getString("customer_name"),
                            resultSet.getDate("txn_date").toLocalDate(),
                            resultSet.getTime("txn_time").toLocalTime(),
                            resultSet.getString("description"),
                            resultSet.getInt("rewards"),
                            resultSet.getBigDecimal("amt"),
                            resultSet.getString("debit_credit"),
                            resultSet.getInt("source_row_index"),
                            resultSet.getLong("id")
                    ));
                }
            }
            return creditCardTransactions;
        } catch (SQLException e) {
            throw new RuntimeException("Exception occurred while fetching credit card transaction for statement file id " + statementFileId, e);
        }

    }
}
