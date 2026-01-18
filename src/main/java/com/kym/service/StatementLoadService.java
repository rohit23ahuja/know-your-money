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


    public StatementLoadService() {
        this.statementFileWriter = new StatementFileWriter();
        this.statementCellReader = new StatementCellReader();
    }

    public long loadStatement(String accountName, String accountNumber, String statementType, String bankCode, String originalFileName) {
        StatementFile statementFile = new StatementFile(accountName, accountNumber, statementType, bankCode, originalFileName);
        long statementFileId = statementFileWriter.writeStatementFile(statementFile);
        List<StatementCell> statementCells = statementCellReader.readStatementCells(statementFileId, originalFileName);
        StatementCellWriter statementCellWriter = new StatementCellWriter(statementFileId);
        int[] updateCount = statementCellWriter.writeStatementCells(statementCells);
        return  statementFileId;
    }
}
