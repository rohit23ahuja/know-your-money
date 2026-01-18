package com.kym;

import com.kym.reader.BankTransactionReader;
import com.kym.service.StatementLoadService;
import com.kym.service.StatementStructureService;

public class App {
    public static void main(String[] args) {

        StatementLoadService statementLoadService = new StatementLoadService();
        long statementFileId = statementLoadService.loadStatement(args[0], args[1], args[2], args[3], args[4]);

        StatementStructureService statementStructureService = new StatementStructureService(statementFileId);
        statementStructureService.detectStatementStructure();

        BankTransactionReader bankTransactionReader = new BankTransactionReader(statementFileId);
        bankTransactionReader.readTransactions();
    }
}
