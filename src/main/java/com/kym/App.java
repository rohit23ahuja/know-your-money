package com.kym;

import com.kym.model.StatementCell;
import com.kym.model.StatementFile;
import com.kym.model.StatementStructure;
import com.kym.processor.StatementStructureDetector;
import com.kym.reader.StatementCellReader;
import com.kym.writer.StatementCellWriter;
import com.kym.writer.StatementFileWriter;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {

        StatementFileWriter statementFileWriter = new StatementFileWriter();
        StatementFile statementFile = new StatementFile(args[0], args[1], args[2]);
        long statementFileId = statementFileWriter.writeStatementFile(statementFile);

        StatementCellReader statementCellReader = new StatementCellReader();
        List<StatementCell> statementCells = statementCellReader.readStatementCells(statementFileId, args[2]);

        StatementCellWriter statementCellWriter = new StatementCellWriter();
        int[] updateCount = statementCellWriter.writeStatementCells(statementCells);
        System.out.println(Arrays.toString(updateCount));

        StatementStructureDetector statementStructureDetector = new StatementStructureDetector();
        StatementStructure statementStructure = statementStructureDetector.detect(statementFileId);
        System.out.println(statementStructure);
    }
}
