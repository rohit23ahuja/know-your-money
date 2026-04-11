package com.kym.service;

import com.kym.api.ProcessStatementResponse;
import com.kym.dto.StatementLoadResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StatementProcessingService {
    private final StatementLoadService statementLoadService;
    private final StatementStructureService statementStructureService;
    private final TransactionParsingService transactionParsingService;
    private final TransactionCategorizationService transactionCategorizationService;
    private final StatementDetailService statementDetailService;


    public StatementProcessingService(StatementLoadService statementLoadService,
                                      StatementStructureService statementStructureService,
                                      TransactionParsingService transactionParsingService,
                                      TransactionCategorizationService transactionCategorizationService,
                                      StatementDetailService statementDetailService) {
        this.statementLoadService = statementLoadService;
        this.statementStructureService = statementStructureService;
        this.transactionParsingService = transactionParsingService;
        this.transactionCategorizationService = transactionCategorizationService;
        this.statementDetailService = statementDetailService;
    }

    public ProcessStatementResponse processStatement(MultipartFile uploadedStatement) {
        StatementLoadResponseDTO statementLoadResponseDTO = statementLoadService.loadStatement(uploadedStatement);

        Long statementStructureId = statementStructureService.detectStatementStructure(statementLoadResponseDTO.statementFileId());

        Long statementDetailId = statementDetailService.detectStatementDetail(statementLoadResponseDTO.statementFileId());

        Integer parsedTransactionCount = transactionParsingService.readTransactions(statementLoadResponseDTO.statementFileId());

        int[] affectedTransactions = transactionCategorizationService.categorize(statementLoadResponseDTO.statementFileId());

        return new ProcessStatementResponse(statementLoadResponseDTO.statementFileId(),
                statementLoadResponseDTO.insertedRowCount(),
                statementDetailId,
                statementStructureId,
                parsedTransactionCount,
                affectedTransactions.length);
    }
}
