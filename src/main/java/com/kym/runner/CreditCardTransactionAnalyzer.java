package com.kym.runner;

import com.kym.service.StatementLoadService;
import com.kym.service.StatementStructureService;
import com.kym.service.TransactionCategorizationService;
import com.kym.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreditCardTransactionAnalyzer implements CommandLineRunner {

    private final StatementLoadService statementLoadService;
    private final StatementStructureService statementStructureService;
    private final TransactionService transactionService;
    private final TransactionCategorizationService transactionCategorizationService;

    public CreditCardTransactionAnalyzer(StatementLoadService statementLoadService, StatementStructureService statementStructureService, TransactionService transactionService, TransactionCategorizationService transactionCategorizationService) {
        this.statementLoadService = statementLoadService;
        this.statementStructureService = statementStructureService;
        this.transactionService = transactionService;
        this.transactionCategorizationService = transactionCategorizationService;
    }


    @Override
    public void run(String... args) throws Exception {
        long statementFileId = statementLoadService.loadStatement(args[0], args[1], args[2], args[3], args[4]);
        statementStructureService.detectStatementStructure(statementFileId);
        transactionService.readTransactions(statementFileId);
        transactionCategorizationService.categorize(statementFileId);

    }
}
