package com.kym.controller;

import com.kym.model.StatementCell;
import com.kym.model.StatementFile;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementFileWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StatementProcessingController {

    private final StatementFileWriter statementFileWriter;
    private final StatementCellWriter statementCellWriter;

    @Autowired
    public StatementProcessingController(StatementFileWriter statementFileWriter,
                                         StatementCellWriter statementCellWriter) {
        this.statementFileWriter = statementFileWriter;
        this.statementCellWriter = statementCellWriter;
    }

    @PostMapping(path = "/statement/process")
    public String processStatement(@RequestParam("uploadedFile") MultipartFile uploadedFile,
                                   @RequestParam("accountName") String accountName,
                                   @RequestParam("accountNumber") String accountNumber,
                                   @RequestParam("statementType") String statementType,
                                   @RequestParam("bankCode") String bankCode) {
        StatementFile statementFile = new StatementFile(accountName, accountNumber, statementType, bankCode, uploadedFile.getName());
        long statementFileId = statementFileWriter.writeStatementFile(statementFile);
        List<StatementCell> statementCells = readStatementCells(statementFileId, uploadedFile);
        int[] updateCount = statementCellWriter.writeStatementCells(statementFileId, statementCells);
        return "Rows inserted in database: " + updateCount.length;
    }

    public List<StatementCell> readStatementCells(long statementFileId, MultipartFile uploadedFile) {
        try (InputStream inputStream = uploadedFile.getInputStream();
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
            throw new RuntimeException("Fail to read Excel file: " + uploadedFile.getOriginalFilename(), e);
        }
    }
}
