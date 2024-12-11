package com.kym.writer;

import com.kym.model.StatementCell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StatementCellWriter {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/kymdb";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "password";
    private static final String SQL_INSERT_STATEMENT_CELL = "insert into statement_cells(cell, text) values(?, ?)";

    public int[] writeStatementCells(List<StatementCell> statementCells) {
        try (Connection connection = DriverManager.getConnection(
                JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_STATEMENT_CELL)) {
            for (StatementCell cell: statementCells) {
                preparedStatement.setString(1, cell.cell());
                preparedStatement.setString(2, cell.text());
                preparedStatement.addBatch();
            }
            return  preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
