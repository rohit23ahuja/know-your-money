package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.entity.StatementFile;
import com.kym.exception.StatementProcessingException;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class StatementFileService {

    private static final String EXCEPTION_MESSAGE_FORMAT = "Statement file ids: %s exists for month year: %s and type: %s. " +
            "Please send reProcess as true for processing again. Note - Existing transactions will be deleted in case of re-processing.";

    private final StatementFileRepository statementFileRepository;

    public StatementFileService(StatementFileRepository statementFileRepository) {
        this.statementFileRepository = statementFileRepository;
    }

    public StatementFile saveStatementFile(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        LocalDate statementYearMonth = parseStatementYearMonth(uploadedStatement.getOriginalFilename());
        List<StatementFile> existingStatementFiles = statementFileRepository
                .findByStatementYearMonthAndStatementType(statementYearMonth, processStatementRequest.statementType());
        if (existingStatementFiles != null && !existingStatementFiles.isEmpty()) {
            if (processStatementRequest.reProcess()) {
                statementFileRepository.deleteAllByIdInBatch(existingStatementFiles
                        .stream()
                        .map(StatementFile::getId).toList());
            } else {
                List<String> existingStatementFileIds = existingStatementFiles
                        .stream()
                        .map(statementFile -> statementFile.getId().toString())
                        .toList();
                throw new StatementProcessingException(
                        String.format(EXCEPTION_MESSAGE_FORMAT,
                                existingStatementFileIds,
                                statementYearMonth,
                                processStatementRequest.statementType()));
            }
        }
        StatementFile statementFile = new StatementFile(uploadedStatement.getOriginalFilename(),
                statementYearMonth,
                processStatementRequest.statementType());
        return statementFileRepository.save(statementFile);
    }

    public LocalDate parseStatementYearMonth(String fileName) {
        String monthYearString = fileName.substring(0, 7);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMyyyy", Locale.ENGLISH);
        YearMonth parsedYearMonth = YearMonth.parse(monthYearString, dateTimeFormatter);
        return parsedYearMonth.atDay(1);
    }
}
