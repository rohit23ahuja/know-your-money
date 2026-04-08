package com.kym.dto;

public record CreditCardTransactionCategorization(long statementFileId, long transactionId, String transactionCategorization) {
}
