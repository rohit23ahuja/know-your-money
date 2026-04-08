package com.kym.api;

public record ProcessStatementRequest(String accountName, String accountNumber, String statementType, String bankCode) {
}
