package com.kym.service;

import com.kym.detector.AccountStatementStructureDetector;
import com.kym.model.StatementCell;
import com.kym.model.StatementFile;
import com.kym.model.StatementStructure;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementFileWriter;
import com.kym.writer.StatementStructureWriter;

import java.util.List;

public class StatementStructureService {

    private final long statementFileId;
    private final StatementCellWriter statementCellWriter;
    private final AccountStatementStructureDetector accountStatementStructureDetector;
    private final StatementStructureWriter statementStructureWriter;
    private final StatementFileWriter statementFileWriter;

    public StatementStructureService(long statementFileId) {
        this.statementFileId = statementFileId;
        statementStructureWriter = new StatementStructureWriter(statementFileId);
        accountStatementStructureDetector = new AccountStatementStructureDetector(statementFileId);
        statementCellWriter = new StatementCellWriter(statementFileId);
        statementFileWriter = new StatementFileWriter();
    }

    public void detectStatementStructure() {
        List<StatementCell> statementCells = statementCellWriter.getStatementCells();
        StatementFile statementFile = statementFileWriter.getStatementFile(statementFileId);
        if("credit-card-statement".equals(statementFile.statementType())) {

        } else {
            StatementStructure statementStructure = accountStatementStructureDetector.detect(statementCells);
        }

        statementStructureWriter.write(statementStructure);
    }
}
