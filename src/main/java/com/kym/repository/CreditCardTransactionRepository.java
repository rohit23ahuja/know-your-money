package com.kym.repository;

import com.kym.entity.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {

    List<CreditCardTransaction> findByStatementFileId(Long statementFileId);
}
