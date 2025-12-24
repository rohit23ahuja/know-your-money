package com.kym.writer;

import com.kym.model.StatementStructure;

import java.sql.*;

import static com.kym.util.Constants.*;

public class StatementStructureWriter {

    private static final String SQL_INSERT_STATEMENT_STRUCTURE =
            """
            insert into statement_structure(statement_file_id, header_row_index, date_col_index, narration_col_index, 
            debit_col_index, credit_col_index, balance_col_index, data_start_row_index, data_end_row_index) 
            values(?,?,?,?,?,?,?,?,?)
            returning id
            """;

    public long write(StatementStructure statementStructure) {
        long statementStructureId = 0l;
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_STRUCTURE)) {

            preparedStatement.setLong(1, statementStructure.statementFileId());
            preparedStatement.setInt(2, statementStructure.headerRowIndex());
            preparedStatement.setInt(3, statementStructure.dateColIndex());
            preparedStatement.setInt(4, statementStructure.narrationColIndex());
            preparedStatement.setInt(5, statementStructure.debitColIndex());
            preparedStatement.setInt(6, statementStructure.creditColIndex());
            preparedStatement.setInt(7, statementStructure.balanceColIndex());
            preparedStatement.setInt(8, statementStructure.dataStartRowIndex());
            preparedStatement.setInt(9, statementStructure.dataEndRowIndex());

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
