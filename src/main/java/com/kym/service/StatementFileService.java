package com.kym.service;

import com.kym.api.ProcessStatementRequest;
import com.kym.entity.StatementFile;
import com.kym.exception.StatementProcessingException;
import com.kym.repository.StatementFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class StatementFileService {

    private static final String EXCEPTION_MESSAGE_FORMAT = "Statement file ids: %s exists for month year: %s and type: %s. " +
            "Please send reProcess as true for processing again. Note - Existing transactions will be deleted in case of re-processing.";

    private final StatementFileRepository statementFileRepository;

    public StatementFileService(StatementFileRepository statementFileRepository) {
        this.statementFileRepository = statementFileRepository;
    }

    public Long saveStatementFile(MultipartFile uploadedStatement, ProcessStatementRequest processStatementRequest) {
        String statementMonthYear = parseStatementMonthYear(uploadedStatement.getOriginalFilename());
        List<StatementFile> existingStatementFiles = statementFileRepository
                .findByStatementMonthYearAndStatementType(statementMonthYear, processStatementRequest.statementType());
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
                                statementMonthYear,
                                processStatementRequest.statementType()));
            }
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
