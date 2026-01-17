package com.kym.detector;

import com.kym.model.StatementCell;
import com.kym.model.StatementStructure;

import java.util.List;

public interface StatementStructureDetector {
    StatementStructure detect(List<StatementCell> statementCells);
}
