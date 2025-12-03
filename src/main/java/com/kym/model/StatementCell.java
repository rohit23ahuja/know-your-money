package com.kym.model;

public record StatementCell(long statementFileId, Integer rowIndex, Integer columnIndex, String cellRef,
                            String rawValueText) {
}
