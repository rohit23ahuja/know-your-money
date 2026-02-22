package com.kym.model;

import java.math.BigDecimal;

public record CreditCardTransactionDTO(Long id, Long statementFileId, String txnType, String customerName,
                                       String txnDateTime, String description, Integer rewards, BigDecimal amt,
                                       String debitCredit, String transactionCategorization) {

}
