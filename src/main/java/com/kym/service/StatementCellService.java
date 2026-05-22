package com.kym.service;

import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.reader.StatementCellReader;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class StatementCellService {
    private final StatementCellReader statementCellReader;
    private final StatementCellRepository statementCellRepository;

    public StatementCellService(StatementCellReader statementCellReader,
                                StatementCellRepository statementCellRepository) {
        this.statementCellReader = statementCellReader;
        this.statementCellRepository = statementCellRepository;
    }

    public List<StatementCell> readStatementCells(StatementFile statementFile, MultipartFile uploadedStatement) {
        List<StatementCell> statementCells = statementCellReader.readStatementCells(statementFile.getId(), uploadedStatement);
        return statementCellRepository.saveAll(statementCells);
    }
}
