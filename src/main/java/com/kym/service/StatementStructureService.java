package com.kym.service;

import com.kym.detector.AccountStatementStructureDetector;
import com.kym.detector.CreditCardStatementStructureDetector;
import com.kym.model.CreditCardStatementStructure;
import com.kym.model.StatementCell;
import com.kym.model.StatementFile;
import com.kym.model.AccountStatementStructure;
import com.kym.writer.CreditCardStatementStructureWriter;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementFileWriter;
import com.kym.writer.AccountStatementStructureWriter;

import java.util.List;

public class StatementStructureService {

    private final long statementFileId;
    private final StatementCellWriter statementCellWriter;
    private final StatementFileWriter statementFileWriter;

    public StatementStructureService(long statementFileId) {
        this.statementFileId = statementFileId;
        statementCellWriter = new StatementCellWriter(statementFileId);
        statementFileWriter = new StatementFileWriter();
    }

    public void detectStatementStructure() {
        List<StatementCell> statementCells = statementCellWriter.getStatementCells();
        StatementFile statementFile = statementFileWriter.getStatementFile(statementFileId);
        if("credit-card-statement".equals(statementFile.statementType())) {
            CreditCardStatementStructureDetector creditCardStatementStructureDetector = new CreditCardStatementStructureDetector(statementFileId);
            CreditCardStatementStructure creditCardStatementStructure = creditCardStatementStructureDetector.detect(statementCells);
            CreditCardStatementStructureWriter creditCardStatementStructureWriter = new CreditCardStatementStructureWriter(statementFileId);
            creditCardStatementStructureWriter.write(creditCardStatementStructure);
        } else {
            AccountStatementStructureDetector accountStatementStructureDetector = new AccountStatementStructureDetector(statementFileId);
            AccountStatementStructure accountStatementStructure = accountStatementStructureDetector.detect(statementCells);
            AccountStatementStructureWriter accountStatementStructureWriter = new AccountStatementStructureWriter(statementFileId);
            accountStatementStructureWriter.write(accountStatementStructure);
        }

    }
}
