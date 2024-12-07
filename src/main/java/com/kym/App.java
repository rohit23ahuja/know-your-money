package com.kym;

import com.kym.reader.PdfReader;

public class App {
    public static void main(String[] args) {
        PdfReader pdfReader = new PdfReader();
        System.out.println(pdfReader.extractText("E:/AccountStatements/IciciBankOct24.pdf"));
    }
}
