package com.kym.controller;

import com.kym.api.ProcessStatementRequest;
import com.kym.api.ProcessStatementResponse;
import com.kym.exception.StatementProcessingException;
import com.kym.service.StatementProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StatementProcessingController {

    private final StatementProcessingService statementProcessingService;

    @Autowired
    public StatementProcessingController(StatementProcessingService statementProcessingService) {
        this.statementProcessingService = statementProcessingService;
    }

    @PostMapping(path = "/statement/process")
    public ResponseEntity<ProcessStatementResponse> processStatement(@RequestParam("uploadedStatement") MultipartFile uploadedStatement,
                                                                     @RequestPart("processStatementRequest") ProcessStatementRequest processStatementRequest) {
        if (uploadedStatement.getOriginalFilename() == null
                || uploadedStatement.getOriginalFilename().isBlank())
            throw new StatementProcessingException("File name cannot be blank.");
        ProcessStatementResponse processStatementResponse = statementProcessingService.processStatement(uploadedStatement, processStatementRequest);
        return ResponseEntity.ok(processStatementResponse);
    }

}
