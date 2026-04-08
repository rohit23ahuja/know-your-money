package com.kym.controller;

import com.kym.api.ProcessStatementRequest;
import com.kym.api.ProcessStatementResponse;
import com.kym.service.StatementProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ProcessStatementResponse processStatement(@RequestParam("uploadedStatement") MultipartFile uploadedStatement,
                                                     @RequestPart("statementProcessRequest") ProcessStatementRequest processStatementRequest) {
        return statementProcessingService.processStatement(uploadedStatement, processStatementRequest);
    }


}
