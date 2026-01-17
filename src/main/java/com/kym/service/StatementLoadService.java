package com.kym.service;

import com.kym.model.StatementCell;
import com.kym.model.StatementFile;
import com.kym.reader.StatementCellReader;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementFileWriter;

import java.util.List;

public class StatementLoadService {

    private final StatementFileWriter statementFileWriter;
    private final StatementCellReader statementCellReader;
    private final StatementCellWriter statementCellWriter;

    public StatementLoadService() {
        this.statementFileWriter = new StatementFileWriter();
        this.statementCellReader = new StatementCellReader();
        this.statementCellWriter = new StatementCellWriter();
    }

    public long loadStatement(String accountName, String accountNumber, String statementType, String bankCode, String originalFileName) {
        StatementFile statementFile = new StatementFile(accountName, accountNumber, statementType, bankCode, originalFileName);
        long statementFileId = statementFileWriter.writeStatementFile(statementFile);
        List<StatementCell> statementCells = statementCellReader.readStatementCells(statementFileId, originalFileName);
        int[] updateCount = statementCellWriter.writeStatementCells(statementCells);
        return  statementFileId;
    }
}
