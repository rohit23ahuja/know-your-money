package com.kym.api;

public record ProcessStatementResponse(Long statementFileId,
                                       Integer statementRowsCount,
                                       Long statementDetailId,
                                       Long statementStructureId,
                                       Integer parsedTransactionsCount,
                                       Integer categorizedTransactionsCount) {
}
