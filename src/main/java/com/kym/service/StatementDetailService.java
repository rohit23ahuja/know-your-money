package com.kym.service;

import com.kym.detector.StatementDetailDetector;
import com.kym.entity.StatementCell;
import com.kym.entity.StatementDetail;
import com.kym.entity.StatementFile;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatementDetailService {
    private final StatementDetailRepository statementDetailRepository;
    private final StatementDetailDetector statementDetailDetector;

    public StatementDetailService(StatementDetailRepository statementDetailRepository, StatementDetailDetector statementDetailDetector) {
        this.statementDetailRepository = statementDetailRepository;
        this.statementDetailDetector = statementDetailDetector;
    }

    public StatementDetail parseStatementDetail(StatementFile statementFile, List<StatementCell> statementCells) {
        StatementDetail statementDetail = statementDetailDetector.detect(statementFile.getId(), statementCells);
        return statementDetailRepository.save(statementDetail);
    }
}
