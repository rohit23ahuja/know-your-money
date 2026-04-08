package com.kym.repository;

import com.kym.entity.AccountStatementStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountStatementStructureRepository extends JpaRepository<AccountStatementStructure, Long> {
    Optional<AccountStatementStructure> findByStatementFileId(Long statementFileId);
}
