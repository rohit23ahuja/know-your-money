package com.kym.writer;

import com.kym.model.StatementCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static  com.kym.util.Constants.*;
public class StatementCellWriter {

    private static final String SQL_INSERT_STATEMENT_CELL = """
    insert into 
        statement_cell(statement_file_id, row_index, col_index, cell_ref, raw_value_text) 
        values(?, ?, ?, ?, ?)
    """;

    private static final String SQL_SELECT_STATEMENT_CELL = """
            select row_index, col_index, cell_ref, raw_value_text from statement_cell where statement_file_id = ? 
            order by row_index, col_index;
            """;

    public int[] writeStatementCells(List<StatementCell> statementCells) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_CELL)) {
            for (StatementCell cell: statementCells) {
                preparedStatement.setLong(1, cell.statementFileId());
                preparedStatement.setInt(2, cell.rowIndex());
                preparedStatement.setInt(3, cell.columnIndex());
                preparedStatement.setString(4, cell.cellRef());
                preparedStatement.setString(5, cell.rawValueText());
                preparedStatement.addBatch();
            }
            return  preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StatementCell> getStatementCells(long statementFileId) {
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
