package com.kym.service;

import com.kym.detector.StatementDetailDetector;
import com.kym.entity.StatementCell;
import com.kym.entity.StatementDetail;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementDetailService {
    private final StatementCellRepository statementCellRepository;
    private final StatementDetailRepository statementDetailRepository;
    private final StatementDetailDetector statementDetailDetector;

    public StatementDetailService(StatementCellRepository statementCellRepository, StatementDetailRepository statementDetailRepository, StatementDetailDetector statementDetailDetector) {
        this.statementCellRepository = statementCellRepository;
        this.statementDetailRepository = statementDetailRepository;
        this.statementDetailDetector = statementDetailDetector;
    }

    public Long parseStatementDetail(Long statementFileId) {
        List<StatementCell> statementCells = statementCellRepository.findByStatementFileId(statementFileId);
        StatementDetail statementDetail = statementDetailDetector.detect(statementFileId, statementCells);
        StatementDetail savedStatementDetail = statementDetailRepository.save(statementDetail);
        return savedStatementDetail.getId();
    }
}
