package com.kym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardTransaction(long statementFileId, String transactionType, String customerName,
                                    LocalDateTime transactionDateTime, String description,
                                    Integer rewards, BigDecimal amt, String debitCredit, Integer sourceRowIndex, long id, String transactionCategorization) {
}
