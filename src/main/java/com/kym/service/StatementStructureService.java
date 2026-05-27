package com.kym.service;

import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.entity.StatementStructure;

import java.util.List;

public interface StatementStructureService<S extends StatementStructure> {

    S parseAndSaveStatementStructure(StatementFile statementFile, List<StatementCell> statementCells);

    String getType();
}
