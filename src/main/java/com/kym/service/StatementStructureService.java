package com.kym.service;

import com.kym.detector.AccountStatementStructureDetector;
import com.kym.detector.CreditCardStatementStructureDetector;
import com.kym.entity.*;
import com.kym.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementStructureService {

    private final StatementCellRepository statementCellRepository;
    private final CreditCardStatementStructureDetector creditCardStatementStructureDetector;
    private final CreditCardStatementStructureRepository creditCardStatementStructureRepository;
    private final AccountStatementStructureDetector accountStatementStructureDetector;
    private final AccountStatementStructureRepository accountStatementStructureRepository;
    private final StatementDetailRepository statementDetailRepository;

    public StatementStructureService(StatementCellRepository statementCellRepository,
                                     CreditCardStatementStructureDetector creditCardStatementStructureDetector,
                                     CreditCardStatementStructureRepository creditCardStatementStructureRepository,
                                     AccountStatementStructureDetector accountStatementStructureDetector,
                                     AccountStatementStructureRepository accountStatementStructureRepository,
                                     StatementDetailRepository statementDetailRepository) {
        this.statementCellRepository = statementCellRepository;
        this.creditCardStatementStructureDetector = creditCardStatementStructureDetector;
        this.creditCardStatementStructureRepository = creditCardStatementStructureRepository;
        this.accountStatementStructureDetector = accountStatementStructureDetector;
        this.accountStatementStructureRepository = accountStatementStructureRepository;
        this.statementDetailRepository = statementDetailRepository;
    }


    public Long detectStatementStructure(Long statementFileId) {
        List<StatementCell> statementCells = statementCellRepository.findByStatementFileId(statementFileId);
        StatementDetail statementDetail = statementDetailRepository.findByStatementFileId(statementFileId);
        if("credit-card-statement".equals(statementDetail.getStatementType())) {
            CreditCardStatementStructure creditCardStatementStructure = creditCardStatementStructureDetector.detect(statementFileId, statementCells);
            CreditCardStatementStructure savedCreditCardStatementStructure = creditCardStatementStructureRepository.save(creditCardStatementStructure);
            return savedCreditCardStatementStructure.getId();
        } else {
            AccountStatementStructure accountStatementStructure = accountStatementStructureDetector.detect(statementFileId, statementCells);
            AccountStatementStructure savedAccountStatementStructure = accountStatementStructureRepository.save(accountStatementStructure);
            return savedAccountStatementStructure.getId();
        }

    }
}
