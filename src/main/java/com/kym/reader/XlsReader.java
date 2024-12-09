package com.kym.reader;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XlsReader {
    private static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{2}$";
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

    public void printTransactions(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
            Sheet sheet1 = wb.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            for (Row row: sheet1) {
                boolean rowOfTransaction = false;
                for (Cell cell: row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    //System.out.println(cellRef.formatAsString());

                    String text = formatter.formatCellValue(cell);
                    //System.out.println(text);

                    if (cell.getColumnIndex() ==0 && cell.getCellType() == CellType.STRING && isValidDate(cell.getStringCellValue())) {
                        rowOfTransaction = true;
                    }

                    if (rowOfTransaction){
                        switch (cell.getCellType()) {
                            case STRING -> System.out.print(cell.getRichStringCellValue().getString());
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    System.out.print(cell.getDateCellValue());
                                } else {
                                    System.out.print(cell.getNumericCellValue());
                                }
                            }
                            case BOOLEAN -> System.out.print(cell.getBooleanCellValue());
                            case FORMULA -> System.out.print(cell.getCellFormula());
                            case BLANK -> System.out.print("");
                            default -> System.out.print("");
                        }
                        System.out.print(" ");
                    }
                }
                System.out.println(" ");
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidDate(String date) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}
