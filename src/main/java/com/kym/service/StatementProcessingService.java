package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.api.ProcessStatementResponse;
import com.kym.dto.StatementLoadRequestDTO;
import com.kym.dto.StatementLoadResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StatementProcessingService {
    private final StatementLoadService statementLoadService;
    private final StatementStructureService statementStructureService;
    private final TransactionParsingService transactionParsingService;
    private final TransactionCategorizationService transactionCategorizationService;


    public StatementProcessingService(StatementLoadService statementLoadService,
                                      StatementStructureService statementStructureService,
                                      TransactionParsingService transactionParsingService,
                                      TransactionCategorizationService transactionCategorizationService) {
        this.statementLoadService = statementLoadService;
        this.statementStructureService = statementStructureService;
        this.transactionParsingService = transactionParsingService;
        this.transactionCategorizationService = transactionCategorizationService;
    }

    public ProcessStatementResponse processStatement(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        StatementLoadRequestDTO statementLoadRequestDTO = new StatementLoadRequestDTO(uploadedStatement, processStatementRequest.accountName(), processStatementRequest.accountNumber(), processStatementRequest.statementType(), processStatementRequest.bankCode(), uploadedStatement.getName());
        StatementLoadResponseDTO statementLoadResponseDTO = statementLoadService.loadStatement(statementLoadRequestDTO);

        Long statementStructureId = statementStructureService.detectStatementStructure(statementLoadResponseDTO.statementFileId());

        Integer parsedTransactionCount = transactionParsingService.readTransactions(statementLoadResponseDTO.statementFileId());

        int[] affectedTransactions = transactionCategorizationService.categorize(statementLoadResponseDTO.statementFileId());

        return new ProcessStatementResponse(statementLoadResponseDTO.statementFileId(),
                statementLoadResponseDTO.insertedRowCount(),
                statementStructureId,
                parsedTransactionCount,
                affectedTransactions.length);
    }
}
