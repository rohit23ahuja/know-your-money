package com.kym.writer;

import com.kym.model.StatementFile;

import java.sql.*;

import static com.kym.util.Constants.*;

public class StatementFileWriter {

    private static final String SQL_INSERT_STATEMENT_FILE = """ 
            insert into statement_file(account_name, account_number, statement_type, bank_code,  original_filename) 
            values(?, ?, ?, ?, ?)
            returning id
            """;

    private static final String SQL_GET_STATEMENT_FILE = """
            select account_name, account_number, statement_type, bank_code, original_filename 
            from statement_file where id=39;
            """;

    public long writeStatementFile(StatementFile statementFile) {
        long statementFileId = 0l;
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_FILE)) {

            preparedStatement.setString(1, statementFile.accountName());
            preparedStatement.setString(2, statementFile.accountNumber());
            preparedStatement.setString(3, statementFile.statementType());
            preparedStatement.setString(4, statementFile.bankCode());
            preparedStatement.setString(5, statementFile.originalFileName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    statementFileId = resultSet.getLong("id");
                    System.out.println("Inserted statement_file ID = " + statementFileId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statementFileId;
    }

    public StatementFile getStatementFile(long statementFileId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_STATEMENT_FILE)) {
            preparedStatement.setLong(1, statementFileId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("No statement file for id=" + statementFileId);
                }
                return new StatementFile(
                        resultSet.getString("account_name"),
                        resultSet.getString("account_number"),
                        resultSet.getString("statement_type"),
                        resultSet.getString("bank_code"),
                        resultSet.getString("original_filename")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
