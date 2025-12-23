package com.kym.repository;

import com.kym.model.StatementCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.kym.util.Constants.*;

public class StatementCellRepository {

    private static final String SQL_SELECT_STATEMENT_CELL = """
            select row_index, col_index, cell_ref, raw_value_text from statement_cell where statement_file_id = ? 
            order by row_index, col_index;
            """;

    private List<StatementCell> getStatementCells(long statementFileId) {
        List<StatementCell> statementCells = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_STATEMENT_CELL)) {

            preparedStatement.setLong(1, statementFileId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    StatementCell statementCell = new StatementCell(statementFileId,
                            resultSet.getInt("row_index"),
                            resultSet.getInt("col_index"),
                            resultSet.getString("cell_ref"),
                            resultSet.getString("raw_value_text")
                    );

                    statementCells.add(statementCell);
                }
            }
            return statementCells;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load statement cells for statement file id "+ statementFileId, e);
        }
    }

}
