package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.api.ProcessStatementResponse;
import com.kym.entity.*;
import com.kym.reader.TransactionParsingService;
import com.kym.reader.TransactionParsingServiceResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StatementProcessingService {
    private final StatementFileService statementFileService;
    private final StatementCellService statementCellService;
    private final TransactionCategorizationService transactionCategorizationService;
    private final StatementDetailService statementDetailService;
    private final StatementStructureServiceResolver statementStructureServiceResolver;
    private final TransactionParsingServiceResolver transactionParsingServiceResolver;


    public StatementProcessingService(StatementFileService statementFileService,
                                      StatementCellService statementCellService,
                                      TransactionCategorizationService transactionCategorizationService,
                                      StatementDetailService statementDetailService,
                                      StatementStructureServiceResolver statementStructureServiceResolver,
                                      TransactionParsingServiceResolver transactionParsingServiceResolver) {
        this.statementFileService = statementFileService;
        this.statementCellService = statementCellService;
        this.transactionCategorizationService = transactionCategorizationService;
        this.statementDetailService = statementDetailService;
        this.statementStructureServiceResolver = statementStructureServiceResolver;
        this.transactionParsingServiceResolver = transactionParsingServiceResolver;
    }

    public ProcessStatementResponse processStatement(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        StatementFile statementFile = statementFileService.saveStatementFile(uploadedStatement, processStatementRequest);
        List<StatementCell> statementCells = statementCellService.readStatementCells(statementFile, uploadedStatement);
        StatementDetail statementDetail = statementDetailService.parseStatementDetail(statementFile, statementCells);
        StatementStructureService statementStructureService = statementStructureServiceResolver.resolve(statementDetail.getStatementType());
        StatementStructure statementStructure = statementStructureService.parseAndSaveStatementStructure(statementFile, statementCells);
        TransactionParsingService transactionParsingService = transactionParsingServiceResolver.resolve(statementDetail.getStatementType());
        List<Transaction> transactions = transactionParsingService.parseAndSaveTransactions(statementFile.getId(), statementStructure);
        int[] affectedTransactions = transactionCategorizationService.categorize(transactions, statementDetail);

        return new ProcessStatementResponse(statementFile.getId(),
                statementCells.size(),
                statementDetail.getId(),
                statementStructure.getId(),
                transactions.size(),
                affectedTransactions.length);
    }
}
