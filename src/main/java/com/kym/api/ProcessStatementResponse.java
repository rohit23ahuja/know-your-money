package com.kym.api;

public record ProcessStatementResponse(Long statementFileId,
                                       Integer statementRowsCount,
                                       Long statementStructureId,
                                       Integer parsedTransactionsCount,
                                       Integer categorizedTransactionsCount) {
}
