package com.kym.controller;

import com.kym.api.PageResponse;
import com.kym.api.TransactionSearchRequest;
import com.kym.dto.CreditCardTransactionDTO;
import com.kym.entity.CreditCardTransaction;
import com.kym.repository.CreditCardTransactionRepository;
import com.kym.specification.CreditCardTransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping(path = "/transactions/search")
    public ResponseEntity<PageResponse<CreditCardTransactionDTO>> search(
            @ModelAttribute TransactionSearchRequest transactionSearchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(search(transactionSearchRequest, pageRequest));
    }

    public PageResponse<CreditCardTransactionDTO> search(TransactionSearchRequest transactionSearchRequest,
                                                         PageRequest pageRequest) {
        Specification<CreditCardTransaction> creditCardTransactionSpecification = CreditCardTransactionSpecification.withFilters(transactionSearchRequest);
        Page<CreditCardTransactionDTO> result = creditCardTransactionRepository
                .findAll(creditCardTransactionSpecification, pageRequest)
                .map(this::convertToDTO);
        return PageResponse.from(result);
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
