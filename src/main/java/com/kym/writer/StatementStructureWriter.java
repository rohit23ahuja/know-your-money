package com.kym.writer;

import com.kym.model.AccountStatementStructure;

import java.sql.*;

import static com.kym.util.Constants.*;

public class StatementStructureWriter {

    private static final String SQL_SELECT_STATEMENT_STRUCTURE =
            """
                    select 
                          statement_file_id, header_row_index, date_col_index, narration_col_index, debit_col_index, 
                          credit_col_index, balance_col_index, data_start_row_index, data_end_row_index 
                    from statement_structure where statement_file_id = ?;
                    """;

    private static final String SQL_INSERT_STATEMENT_STRUCTURE =
            """
                    insert into statement_structure(statement_file_id, header_row_index, date_col_index, narration_col_index, 
                    debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index) 
                    values(?,?,?,?,?,?,?,?,?)
                    returning id
                    """;

    private final long statementFileId;

    public StatementStructureWriter(long statementFileId) {
        this.statementFileId = statementFileId;
    }

    public AccountStatementStructure getStatementStructure(long statementFileId) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_STATEMENT_STRUCTURE)) {
            preparedStatement.setLong(1, statementFileId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("No statement_structure found for statement_file_id=" + statementFileId);
                }
                AccountStatementStructure accountStatementStructure = new AccountStatementStructure(
                        resultSet.getLong("statement_file_id"),
                        resultSet.getInt("header_row_index"),
                        resultSet.getInt("date_col_index"),
                        resultSet.getInt("narration_col_index"),
                        resultSet.getInt("debit_col_index"),
                        resultSet.getInt("credit_col_index"),
                        resultSet.getInt("balance_col_index"),
                        resultSet.getInt("data_start_row_index"),
                        resultSet.getInt("data_end_row_index")
                );
                if (resultSet.next()) {
                    throw new RuntimeException(
                            "Multiple statement_structure rows found for statement_file_id=" + statementFileId
                    );
                }
                return accountStatementStructure;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load statement cells for statement file id " + statementFileId, e);
        }
    }

    public long write(AccountStatementStructure accountStatementStructure) {
        long statementStructureId = 0l;
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_STRUCTURE)) {

            preparedStatement.setLong(1, accountStatementStructure.statementFileId());
            preparedStatement.setInt(2, accountStatementStructure.headerRowIndex());
            preparedStatement.setInt(3, accountStatementStructure.dateColIndex());
            preparedStatement.setInt(4, accountStatementStructure.narrationColIndex());
            preparedStatement.setInt(5, accountStatementStructure.debitColIndex());
            preparedStatement.setInt(6, accountStatementStructure.creditColIndex());
            preparedStatement.setInt(7, accountStatementStructure.balanceColIndex());
            preparedStatement.setInt(8, accountStatementStructure.dataStartRowIndex());
            preparedStatement.setInt(9, accountStatementStructure.dataEndRowIndex());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    statementStructureId = resultSet.getLong("id");
                    System.out.println("Inserted statement_structure id = " + statementStructureId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statementStructureId;
    }


}
