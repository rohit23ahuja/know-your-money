package com.kym.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountTransaction(long statementFileId, LocalDate txnDate, String narration, BigDecimal debitAmount,
                                 BigDecimal creditAmount, BigDecimal balance, Integer sourceRowIndex) {
}
