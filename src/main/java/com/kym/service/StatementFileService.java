package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.entity.StatementFile;
import com.kym.repository.StatementFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class StatementFileService {
    private final StatementFileRepository statementFileRepository;

    public StatementFileService(StatementFileRepository statementFileRepository) {
        this.statementFileRepository = statementFileRepository;
    }

    public Long saveStatementFile(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        String statementMonthYear = parseStatementMonthYear(uploadedStatement.getOriginalFilename());
        StatementFile byStatementMonthYearAndStatementType = statementFileRepository.findByStatementMonthYearAndStatementType(statementMonthYear, processStatementRequest.statementType());
        if (byStatementMonthYearAndStatementType!=null && !processStatementRequest.reProcess()) {
            String format = """
                    Statement file id: %s exists for month year: %s and type: %s. Please send reProcess as true for processing again. Note - Existing transactions will be deleted in case of re-processing.
                    """;
            throw new RuntimeException(
                    String.format(format,
                            byStatementMonthYearAndStatementType.getId(),
                            byStatementMonthYearAndStatementType.getStatementMonthYear(),
                            byStatementMonthYearAndStatementType.getStatementType()));
        }
        StatementFile statementFile = new StatementFile(uploadedStatement.getOriginalFilename(),
                statementMonthYear,
                processStatementRequest.statementType());
        StatementFile savedStatementFile = statementFileRepository.save(statementFile);
        return savedStatementFile.getId();
    }

    public String parseStatementMonthYear(String fileName) {
        String monthYearString = fileName.substring(0, 7);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMyyyy");
        YearMonth parsedYearMonth = YearMonth.parse(monthYearString, dateTimeFormatter);
        return StringUtils.join(monthYearString.substring(0, 3), " ", monthYearString.substring(3));
    }
}
