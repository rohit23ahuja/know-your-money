package com.kym.model;

public record CreditCardTransactionCategorization(long statementFileId, long transactionId, String transactionCategorization) {
}
