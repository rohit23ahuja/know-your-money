package com.kym.api;

import java.time.LocalDate;

public record TransactionSearchRequest(Long statementFileId, String accountHolder, String description,
                                       String category, LocalDate transactionDateFrom, LocalDate transactionDateTo) {
}
