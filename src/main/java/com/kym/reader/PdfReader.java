package com.kym.reader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfReader {
    public String extractText(String filePath){
        PDDocument document = null;
        try {
            document = Loader.loadPDF(new RandomAccessReadBufferedFile(filePath));
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            document.close();
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
