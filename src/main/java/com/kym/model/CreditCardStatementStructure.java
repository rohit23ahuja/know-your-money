package com.kym.model;

public record CreditCardStatementStructure(long statementFileId, Integer headerRowIndex,
                                           Integer transactionTypeColIndex, Integer customerNameColIndex,
                                           Integer dateTimeColIndex, Integer descriptionColIndex,
                                           Integer rewardsColIndex, Integer amtColIndex, Integer debitCreditColIndex,
                                           Integer dataStartRowIndex, Integer dataEndRowIndex) {
}
