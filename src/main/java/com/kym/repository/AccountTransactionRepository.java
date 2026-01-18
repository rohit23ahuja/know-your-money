package com.kym.repository;

import com.kym.model.AccountTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.kym.util.Constants.*;

public class AccountTransactionRepository {

    private static final String SQL_INSERT_BANK_TRANSACTION = """
            insert into 
            account_transaction(statement_file_id, txn_date, narration, debit_amount, credit_amount, balance, source_row_index) 
            values(?, ?, ?, ?, ?, ?, ?);
            """;

    public int[] save(List<AccountTransaction> accountTransactions) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_BANK_TRANSACTION)) {
            for (AccountTransaction accountTransaction : accountTransactions) {
                preparedStatement.setLong(1, accountTransaction.statementFileId());
                preparedStatement.setObject(2, accountTransaction.txnDate());
                preparedStatement.setString(3, accountTransaction.narration());
                preparedStatement.setBigDecimal(4, accountTransaction.debitAmount());
                preparedStatement.setBigDecimal(5, accountTransaction.creditAmount());
                preparedStatement.setBigDecimal(6, accountTransaction.balance());
                preparedStatement.setInt(7, accountTransaction.sourceRowIndex());
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
