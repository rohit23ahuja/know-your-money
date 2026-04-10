package com.kym.service;

import com.kym.dto.StatementLoadRequestDTO;
import com.kym.dto.StatementLoadResponseDTO;
import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.reader.StatementCellReader;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementFileRepository;
import com.kym.util.PropertyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementLoadService {
    private final StatementFileRepository statementFileRepository;
    private final StatementCellReader statementCellReader;
    private final StatementCellRepository statementCellRepository;
    private final PropertyUtils propertyUtils;

    public StatementLoadService(StatementFileRepository statementFileRepository, StatementCellReader statementCellReader, StatementCellRepository statementCellRepository, PropertyUtils propertyUtils) {
        this.statementFileRepository = statementFileRepository;
        this.statementCellReader = statementCellReader;
        this.statementCellRepository = statementCellRepository;
        this.propertyUtils = propertyUtils;
    }

    public StatementLoadResponseDTO loadStatement(StatementLoadRequestDTO statementLoadRequestDTO) {
        StatementFile statementFile = new StatementFile(statementLoadRequestDTO.accountName(),
                statementLoadRequestDTO.accountNumber(),
                statementLoadRequestDTO.statementType(),
                statementLoadRequestDTO.bankCode(),
                statementLoadRequestDTO.originalFileName());
        StatementFile savedStatementFile = statementFileRepository.save(statementFile);
        List<StatementCell> statementCells = statementCellReader.readStatementCells(savedStatementFile.getId(), statementLoadRequestDTO.uploadedStatement());
        StatementCell accountNoStatementCell = statementCells.stream()
                .filter(statementCell -> statementCell.getRawValueText().contains("Credit Card No.:"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account Number could not identified: " + statementLoadRequestDTO.originalFileName()));
        String accountNumber = accountNoStatementCell.getRawValueText().substring(accountNoStatementCell.getRawValueText().length() - 5);
        String accountName = propertyUtils.getPropertiesMap().get(String.join(".", accountNumber, "accountName"));
        String statementType = propertyUtils.getPropertiesMap().get(String.join(".", accountNumber, "statementType"));


        List<StatementCell> statementCellsSaved = statementCellRepository.saveAll(statementCells);
        return new StatementLoadResponseDTO(savedStatementFile.getId(), statementCellsSaved.size());
    }
}
