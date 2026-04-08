package com.kym.controller;

import com.kym.entity.CreditCardTransaction;
import com.kym.dto.CreditCardTransactionDTO;
import com.kym.repository.CreditCardTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    @Autowired
    private CreditCardTransactionRepository creditCardTransactionRepository;

    @GetMapping(path = "/transactions")
    public List<CreditCardTransactionDTO> getTransactions() {
        return creditCardTransactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CreditCardTransactionDTO convertToDTO(CreditCardTransaction creditCardTransaction) {
        return new CreditCardTransactionDTO(
                creditCardTransaction.getId(),
                creditCardTransaction.getStatementFileId(),
                creditCardTransaction.getTxnType(),
                toTitleCase(creditCardTransaction.getCustomerName()),
                formatDateTime(creditCardTransaction.getTxnDateTime()),
                creditCardTransaction.getDescription(),
                creditCardTransaction.getRewards(),
                creditCardTransaction.getAmt(),
                creditCardTransaction.getDebitCredit(),
                creditCardTransaction.getTransactionCategorization());
    }

    public String toTitleCase(String input) {
        return Arrays.stream(input.split("\\s+"))
                .filter(t -> !t.isEmpty())
                .map(String::toLowerCase)
                .map(t -> Character.toUpperCase(t.charAt(0)) + t.substring(1))
                .collect(Collectors.joining(" "));
    }

    public String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
}
