package com.kym.reader;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XlsReader {
    public String extractText(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
            ExcelExtractor extractor = new ExcelExtractor(wb);
            extractor.setFormulasNotResults(true);
            extractor.setIncludeSheetNames(false);
            String text = extractor.getText();
            wb.close();
            return text;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
