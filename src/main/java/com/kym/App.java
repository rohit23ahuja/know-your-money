package com.kym;

import com.kym.reader.PdfReader;
import com.kym.reader.XlsReader;

public class App {
    public static void main(String[] args) {
        //PdfReader pdfReader = new PdfReader();
        //System.out.println(pdfReader.extractText("E:/AccountStatements/IciciBankOct24.pdf"));
        XlsReader xlsReader = new XlsReader();
        System.out.println(xlsReader.extractText("E:/AccountStatements/HdfcBankOct24.xls"));
    }
}
