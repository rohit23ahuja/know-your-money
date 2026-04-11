package com.kym.repository;

import com.kym.entity.StatementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementDetailRepository extends JpaRepository<StatementDetail, Long> {

    StatementDetail findByStatementFileId(Long statementFileId);
}
