package com.kym.controller;

import com.kym.api.ProcessStatementRequest;
import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementFileRepository;
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

    private final StatementFileRepository statementFileRepository;
    private final StatementCellRepository statementCellRepository;

    @Autowired
    public StatementProcessingController(StatementFileRepository statementFileRepository,
                                         StatementCellRepository statementCellRepository) {
        this.statementFileRepository = statementFileRepository;
        this.statementCellRepository = statementCellRepository;
    }

    @PostMapping(path = "/statement/process")
    public String processStatement(@RequestParam("uploadedStatement") MultipartFile uploadedStatement,
                                   @RequestParam("statementProcessRequest") ProcessStatementRequest processStatementRequest) {
        StatementFile statementFile = new StatementFile(processStatementRequest.accountName(),
                processStatementRequest.accountNumber(),
                processStatementRequest.statementType(),
                processStatementRequest.bankCode(),
                uploadedStatement.getName());
        StatementFile savedStatementFile = statementFileRepository.save(statementFile);
        List<StatementCell> statementCells = readStatementCells(savedStatementFile.getId(), uploadedStatement);
        List<StatementCell> statementCellsSaved = statementCellRepository.saveAll(statementCells);
        return "Rows inserted in database: " + statementCellsSaved.size();
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
