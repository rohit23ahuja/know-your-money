package com.kym.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StatementProcessingController {

    public String processStatement(@RequestParam("statementFile") MultipartFile statementFile,
                                   @RequestParam("accountName") String accountName,
                                   @RequestParam("accountNumber") String accountNumber,
                                   @RequestParam("statementType") String statementType,
                                   @RequestParam("bankCode") String bankCode) {

    }
}
