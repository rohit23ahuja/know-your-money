package com.kym;

import com.kym.model.StatementCell;
import com.kym.reader.StatementCellReader;
import com.kym.writer.StatementCellWriter;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        //PdfReader pdfReader = new PdfReader();
        //System.out.println(pdfReader.extractText("E:/AccountStatements/IciciBankOct24.pdf"));
        //XlsReader xlsReader = new XlsReader();
        //System.out.println(xlsReader.extractText("E:/AccountStatements/HdfcBankOct24.xls"));
        //xlsReader.printTransactions("E:/AccountStatements/HdfcBankOct24.xls");
        StatementCellReader statementCellReader = new StatementCellReader();
        StatementCellWriter statementCellWriter = new StatementCellWriter();
        List<StatementCell> statementCells = statementCellReader.readStatementCells("E:/AccountStatements/HdfcBankOct24.xls");
        int[] updateCount = statementCellWriter.writeStatementCells(statementCells);
        System.out.println(Arrays.toString(updateCount));
    }
}
