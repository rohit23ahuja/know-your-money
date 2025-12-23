package com.kym.service;

import com.kym.processor.StatementStructureDetector;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementStructureRepository;

public class StatementStructureService {

    private final StatementCellRepository statementCellRepository;
    private final StatementStructureDetector statementStructureDetector;
    private final StatementStructureRepository statementStructureRepository;

    public StatementStructureService() {
        statementStructureRepository = new StatementStructureRepository();
        statementStructureDetector = new StatementStructureDetector();
        statementCellRepository = new StatementCellRepository();
    }

    public void detectStatementStructure(long statementFileId) {
        
    }
}
