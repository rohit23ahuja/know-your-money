package com.kym.controller;

import com.kym.projection.StatementYearMonthView;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionSearchController {

    @Autowired
    private StatementFileRepository statementFileRepository;

    @Autowired
    private CreditCardTransactionRepository creditCardTransactionRepository;

    @GetMapping("/statement-year-month")
    public List<StatementYearMonthView> getStatementYearMonth() {
        List<StatementYearMonthView> allStatementYearMonthsDesc = statementFileRepository.findAllStatementYearMonthsDesc();
        return allStatementYearMonthsDesc;
    }

    @GetMapping("/customer-name")
    public List<String> getCustomerName() {
        return creditCardTransactionRepository.findCustomerName();
    }

}
