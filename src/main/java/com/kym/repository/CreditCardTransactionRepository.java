package com.kym.repository;

import com.kym.entity.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long>, JpaSpecificationExecutor<CreditCardTransaction> {

    List<CreditCardTransaction> findByStatementFileId(Long statementFileId);

    @Query("select distinct cct.customerName from CreditCardTransaction cct")
    List<String> findCustomerName();
}
