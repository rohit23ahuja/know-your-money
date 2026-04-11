package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.api.ProcessStatementResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StatementProcessingService {
    private final StatementFileService statementFileService;
    private final StatementCellService statementCellService;
    private final StatementStructureService statementStructureService;
    private final TransactionParsingService transactionParsingService;
    private final TransactionCategorizationService transactionCategorizationService;
    private final StatementDetailService statementDetailService;


    public StatementProcessingService(StatementFileService statementFileService,
                                      StatementCellService statementCellService,
                                      StatementStructureService statementStructureService,
                                      TransactionParsingService transactionParsingService,
                                      TransactionCategorizationService transactionCategorizationService,
                                      StatementDetailService statementDetailService) {
        this.statementFileService = statementFileService;
        this.statementCellService = statementCellService;
        this.statementStructureService = statementStructureService;
        this.transactionParsingService = transactionParsingService;
        this.transactionCategorizationService = transactionCategorizationService;
        this.statementDetailService = statementDetailService;
    }

    public ProcessStatementResponse processStatement(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        Long statementFileId = statementFileService.saveStatementFile(uploadedStatement, processStatementRequest);
        Integer statementCellCount = statementCellService.readStatementCells(statementFileId, uploadedStatement);
        Long statementDetailId = statementDetailService.parseStatementDetail(statementFileId);
        Long statementStructureId = statementStructureService.parseStatementStructure(statementFileId);
        Integer parsedTransactionCount = transactionParsingService.readTransactions(statementFileId);
        int[] affectedTransactions = transactionCategorizationService.categorize(statementFileId);

        return new ProcessStatementResponse(statementFileId,
                statementCellCount,
                statementDetailId,
                statementStructureId,
                parsedTransactionCount,
                affectedTransactions.length);
    }
}
