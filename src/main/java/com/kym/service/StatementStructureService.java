package com.kym.service;

import com.kym.detector.AccountStatementStructureDetector;
import com.kym.detector.CreditCardStatementStructureDetector;
import com.kym.entity.CreditCardStatementStructure;
import com.kym.entity.StatementCell;
import com.kym.entity.StatementFile;
import com.kym.entity.AccountStatementStructure;
import com.kym.repository.CreditCardStatementStructureRepository;
import com.kym.repository.StatementCellRepository;
import com.kym.repository.StatementFileRepository;
import com.kym.repository.AccountStatementStructureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementStructureService {

    private final StatementCellRepository statementCellRepository;
    private final StatementFileRepository statementFileRepository;
    private final CreditCardStatementStructureDetector creditCardStatementStructureDetector;
    private final CreditCardStatementStructureRepository creditCardStatementStructureRepository;
    private final AccountStatementStructureDetector accountStatementStructureDetector;
    private final AccountStatementStructureRepository accountStatementStructureRepository;

    public StatementStructureService(StatementCellRepository statementCellRepository,
                                     StatementFileRepository statementFileRepository,
                                     CreditCardStatementStructureDetector creditCardStatementStructureDetector,
                                     CreditCardStatementStructureRepository creditCardStatementStructureRepository,
                                     AccountStatementStructureDetector accountStatementStructureDetector,
                                     AccountStatementStructureRepository accountStatementStructureRepository) {
        this.statementCellRepository = statementCellRepository;
        this.statementFileRepository = statementFileRepository;
        this.creditCardStatementStructureDetector = creditCardStatementStructureDetector;
        this.creditCardStatementStructureRepository = creditCardStatementStructureRepository;
        this.accountStatementStructureDetector = accountStatementStructureDetector;
        this.accountStatementStructureRepository = accountStatementStructureRepository;
    }


    public void detectStatementStructure(long statementFileId) {
        List<StatementCell> statementCells = statementCellRepository.findByStatementFileId(statementFileId);
        StatementFile statementFile = statementFileRepository.findById(statementFileId).get();
        if("credit-card-statement".equals(statementFile.getStatementType())) {
            CreditCardStatementStructure creditCardStatementStructure = creditCardStatementStructureDetector.detect(statementFileId, statementCells);
            creditCardStatementStructureRepository.save(creditCardStatementStructure);
        } else {
            AccountStatementStructure accountStatementStructure = accountStatementStructureDetector.detect(statementFileId, statementCells);
            accountStatementStructureRepository.save(accountStatementStructure);
        }

    }
}
