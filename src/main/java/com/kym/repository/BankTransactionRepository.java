package com.kym.repository;

import com.kym.model.BankTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.kym.util.Constants.*;

public class BankTransactionRepository {

    private static final String SQL_INSERT_BANK_TRANSACTION = """
            insert into 
            bank_transaction(statement_file_id, txn_date, narration, debit_amount, credit_amount, balance, source_row_index) 
            values(?, ?, ?, ?, ?, ?, ?);
            """;

    public int[] save(List<BankTransaction> bankTransactions) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_BANK_TRANSACTION)) {
            for (BankTransaction bankTransaction : bankTransactions) {
                preparedStatement.setLong(1, bankTransaction.statementFileId());
                preparedStatement.setObject(2, bankTransaction.txnDate());
                preparedStatement.setString(3, bankTransaction.narration());
                preparedStatement.setBigDecimal(4, bankTransaction.debitAmount());
                preparedStatement.setBigDecimal(5, bankTransaction.creditAmount());
                preparedStatement.setBigDecimal(6, bankTransaction.balance());
                preparedStatement.setInt(7, bankTransaction.sourceRowIndex());
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
