package com.kym.reader;

import com.kym.model.StatementCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StatementCellReader {

    public List<StatementCell> readStatementCells(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
            Sheet sheet1 = wb.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<StatementCell> statementCells = new ArrayList<>();
            for (Row row : sheet1) {
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    statementCells.add(new StatementCell(cellRef.formatAsString(), formatter.formatCellValue(cell)));
                }
            }
            return statementCells;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
