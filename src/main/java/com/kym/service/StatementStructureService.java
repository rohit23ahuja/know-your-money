package com.kym.service;

import com.kym.detector.StatementStructureDetector;
import com.kym.model.StatementCell;
import com.kym.model.StatementStructure;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementStructureWriter;

import java.util.List;

public class StatementStructureService {

    private final StatementCellWriter statementCellWriter;
    private final StatementStructureDetector statementStructureDetector;
    private final StatementStructureWriter statementStructureWriter;

    public StatementStructureService() {
        statementStructureWriter = new StatementStructureWriter();
        statementStructureDetector = new StatementStructureDetector();
        statementCellWriter = new StatementCellWriter();
    }

    public void detectStatementStructure(long statementFileId) {
        List<StatementCell> statementCells = statementCellWriter.getStatementCells(statementFileId);
        StatementStructure statementStructure = statementStructureDetector.detect(statementFileId, statementCells);
        statementStructureWriter.write(statementStructure);
    }
}
