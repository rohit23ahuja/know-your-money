package com.kym.writer;

import com.kym.model.StatementCell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static  com.kym.util.Constants.*;
public class StatementCellWriter {

    private static final String SQL_INSERT_STATEMENT_CELL = """
    insert into 
        statement_cell(statement_file_id, row_index, col_index, cell_ref, raw_value_text) 
        values(?, ?, ?, ?, ?)
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
}
