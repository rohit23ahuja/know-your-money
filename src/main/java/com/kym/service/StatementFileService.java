package com.kym.service;

import com.kym.entity.StatementFile;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StatementFileService {
    private final StatementFileRepository statementFileRepository;

    public StatementFileService(StatementFileRepository statementFileRepository) {
        this.statementFileRepository = statementFileRepository;
    }

    public Long saveStatementFile(MultipartFile uploadedStatement){

        StatementFile statementFile = new StatementFile(uploadedStatement.getName());
        StatementFile savedStatementFile = statementFileRepository.save(statementFile);
        return savedStatementFile.getId();
    }
}
