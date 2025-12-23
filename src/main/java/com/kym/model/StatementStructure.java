package com.kym.model;

public record StatementStructure(long statementFileId, Integer headerRowIndex, Integer dateColIndex, Integer narrationColIndex,
                                 Integer debitColIndex, Integer creditColIndex, Integer balanceColIndex, Integer dataStartRowIndex,
                                 Integer dataEndRowIndex) {
}
