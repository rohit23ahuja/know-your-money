package com.kym.reader;

import com.kym.model.StatementCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StatementCellReader {

    public List<StatementCell> readStatementCells(long statementFileId, String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<StatementCell> statementCells = new ArrayList<>();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellReference cellRef =
                            new CellReference(row.getRowNum(), cell.getColumnIndex());
                    statementCells.add(new StatementCell(
                            statementFileId,
                            row.getRowNum(),
                            cell.getColumnIndex(),
                            cellRef.formatAsString(),
                            formatter.formatCellValue(cell)
                    ));
                }
            }
            return statementCells;
        } catch (IOException e) {
            throw new RuntimeException("Fail to read Excel file: " +filePath, e);
        }
    }
}
