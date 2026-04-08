package com.kym.repository;

import com.kym.entity.CreditCardStatementStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardStatementStructureRepository extends JpaRepository<CreditCardStatementStructure, Long> {

    CreditCardStatementStructure findByStatementFileId(Long statementFileId);
}
