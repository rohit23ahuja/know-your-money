package com.kym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreditCardTransaction(long statementFileId, String transactionType, String customerName,
                                    LocalDate transactionDate, LocalTime transactionTime, String description,
                                    Integer rewards, BigDecimal amt, String debitCredit, Integer sourceRowIndex, long id, String transactionCategorization) {
}
