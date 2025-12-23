package com.kym.processor;

import com.kym.model.StatementCell;
import com.kym.model.StatementStructure;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kym.util.Constants.*;

public class StatementStructureDetector {

    private static final String SQL_SELECT_STATEMENT_CELL = """
            select row_index, col_index, cell_ref, raw_value_text from statement_cell where statement_file_id = ? 
            order by row_index, col_index;
            """;

    public StatementStructure detect(long statementFileId) {
        List<StatementCell> statementCells = getStatementCells(statementFileId);
        Map<Integer, List<StatementCell>> statementCellsByRowIndex = statementCells.stream().collect(Collectors.groupingBy(StatementCell::rowIndex));
        StatementStructure statementStructure = statementCellsByRowIndex.entrySet().stream()
                .filter(l ->
                        l.getValue().size() >= 7 &&
                                "Date".equals(l.getValue().get(0).rawValueText()) &&
                                "Narration".equals(l.getValue().get(1).rawValueText()) &&
                                "Chq./Ref.No.".equals(l.getValue().get(2).rawValueText()) &&
                                "Value Dt".equals(l.getValue().get(3).rawValueText()) &&
                                "Withdrawal Amt.".equals(l.getValue().get(4).rawValueText()) &&
                                "Deposit Amt.".equals(l.getValue().get(5).rawValueText()) &&
                                "Closing Balance".equals(l.getValue().get(6).rawValueText())
                )
                .map(l-> new StatementStructure(
                        statementFileId,
                        l.getKey(),
                        l.getValue().get(0).columnIndex(),
                        l.getValue().get(1).columnIndex(),
                        l.getValue().get(4).columnIndex(),
                        l.getValue().get(5).columnIndex(),
                        l.getValue().get(6).columnIndex(),
                        0,0))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to detect header row."));

        Integer headerRowIndex = statementStructure.headerRowIndex();
        Integer dateColIndex = statementStructure.dateColIndex();

        Integer dataStartRowIndex =  statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > headerRowIndex)
                .filter(l->
                        isValidDate(l.getValue().get(dateColIndex).rawValueText()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Start of transaction data not found"));


        Integer dataEndRowIndex =  statementCellsByRowIndex
                .entrySet().stream()
                .filter(e -> e.getKey() > dataStartRowIndex)
                .filter(l->
                        !isValidDate(l.getValue().get(dateColIndex).rawValueText()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("End of transaction data not found"));
        --dataEndRowIndex;

        statementStructure = new StatementStructure(statementStructure.statementFileId(),
                headerRowIndex,
                dateColIndex,
                statementStructure.narrationColIndex(),
                statementStructure.debitColIndex(),
                statementStructure.creditColIndex(),
                statementStructure.balanceColIndex(),
                dataStartRowIndex,
                dataEndRowIndex);

        return statementStructure;
    }

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

    public static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/uu")
                        .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(dateStr, formatter);
            return true;   // valid date in expected format
        } catch (DateTimeParseException e) {
            return false;  // invalid or wrong format
        }
    }

}
