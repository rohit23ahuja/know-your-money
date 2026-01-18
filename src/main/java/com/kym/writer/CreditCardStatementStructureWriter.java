package com.kym.writer;

import com.kym.model.CreditCardStatementStructure;

import java.sql.*;

import static com.kym.util.Constants.*;

public class CreditCardStatementStructureWriter {

    private static final String SQL_SELECT_CREDITCARD_STATEMENT_STRUCTURE = """
            select statement_file_id, header_row_index, transactiontype_col_index, customername_col_index,
            datetime_col_index, description_col_index, rewards_col_index, amt_col_index, debitcredit_col_index, data_start_row_index, data_end_row_index 
            from creditcard_statement_structure where statement_file_id= ?;
            """;

    private static final String SQL_INSERT_CREDITCARD_STATEMENT_STRUCTURE = """
            insert into creditcard_statement_structure
            (statement_file_id, header_row_index, transactiontype_col_index, customername_col_index, datetime_col_index, description_col_index, 
            rewards_col_index, amt_col_index, debitcredit_col_index, data_start_row_index, data_end_row_index) 
            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            returning id;
            """;

    private final long statementFileId;

    public CreditCardStatementStructureWriter(long statementFileId) {
        this.statementFileId = statementFileId;
    }

    public CreditCardStatementStructure getCreditCardStatementStructure() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_CREDITCARD_STATEMENT_STRUCTURE)) {
            preparedStatement.setLong(1, statementFileId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("No credit card statement structure found for statement file id " + statementFileId);
                }
                return new CreditCardStatementStructure(
                        resultSet.getLong("statement_file_id"),
                        resultSet.getInt("header_row_index"),
                        resultSet.getInt("transactiontype_col_index"),
                        resultSet.getInt("customername_col_index"),
                        resultSet.getInt("datetime_col_index"),
                        resultSet.getInt("description_col_index"),
                        resultSet.getInt("rewards_col_index"),
                        resultSet.getInt("amt_col_index"),
                        resultSet.getInt("debitcredit_col_index"),
                        resultSet.getInt("data_start_row_index"),
                        resultSet.getInt("data_end_row_index"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load credit card statement structure for file id " + statementFileId, e);
        }
    }

    public long write(CreditCardStatementStructure creditCardStatementStructure) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CREDITCARD_STATEMENT_STRUCTURE)) {
            preparedStatement.setLong(1, creditCardStatementStructure.statementFileId());
            preparedStatement.setInt(2, creditCardStatementStructure.headerRowIndex());
            preparedStatement.setInt(3, creditCardStatementStructure.transactionTypeColIndex());
            preparedStatement.setInt(4, creditCardStatementStructure.customerNameColIndex());
            preparedStatement.setInt(5, creditCardStatementStructure.dateTimeColIndex());
            preparedStatement.setInt(6, creditCardStatementStructure.descriptionColIndex());
            preparedStatement.setInt(7, creditCardStatementStructure.rewardsColIndex());
            preparedStatement.setInt(8, creditCardStatementStructure.amtColIndex());
            preparedStatement.setInt(9, creditCardStatementStructure.debitCreditColIndex());
            preparedStatement.setInt(10, creditCardStatementStructure.dataStartRowIndex());
            preparedStatement.setInt(11, creditCardStatementStructure.dataEndRowIndex());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    long id = resultSet.getLong("id");
                    System.out.println("Inserted credit card statement structure with id = "+ id);
                    return id;
                } else {
                    throw new RuntimeException("Exception occurred while saving credit card statement structure");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception occurred while saving credit card statement structure");
        }
    }
}
