package com.kym.reader;

import com.kym.entity.*;

import java.util.List;

public interface TransactionParsingService<T extends Transaction, K extends StatementStructure> {

    List<T> parseAndSaveTransactions(long statementFileId, K statementStructure);

    String getType();
}
