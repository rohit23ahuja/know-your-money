package com.kym.service;

import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.reader.StatementCellReader;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementLoadService {

    @Autowired
    private StatementFileRepository statementFileRepository;

    @Autowired
    private StatementCellReader statementCellReader;

    @Autowired
    private StatementCellRepository statementCellRepository;

    public long loadStatement(String accountName, String accountNumber, String statementType, String bankCode, String originalFileName) {
        StatementFile statementFile = new StatementFile(accountName, accountNumber, statementType, bankCode, originalFileName);
        StatementFile savedStatementFile = statementFileRepository.save(statementFile);
        List<StatementCell> statementCells = statementCellReader.readStatementCells(savedStatementFile.getId(), originalFileName);
        statementCellRepository.saveAll(statementCells);
        return savedStatementFile.getId();
    }
}
