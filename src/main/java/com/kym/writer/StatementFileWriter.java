package com.kym.writer;

import com.kym.model.StatementFile;

import java.sql.*;

import static com.kym.util.Constants.*;

public class StatementFileWriter {

    private static final String SQL_INSERT_STATEMENT_FILE = """ 
            insert into statement_file(bank_code, account_number, original_filename) 
            values(?, ?, ?)
            returning id
            """;


    public long writeStatementFile(StatementFile statementFile) {
        long statementFileId = 0l;
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_FILE)) {

            preparedStatement.setString(1, statementFile.bankCode());
            preparedStatement.setString(2, statementFile.accountNumber());
            preparedStatement.setString(3, statementFile.originalFileName());

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

}
