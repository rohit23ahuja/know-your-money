package com.kym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "creditcard_transaction")
public class CreditCardTransaction2 {

    @Id
    private Long id;

    private Long statementFileId;

    private String txnType;

    private String customerName;

    @Column(name = "txn_datetime")
    private LocalDateTime txnDateTime;

    private String description;

    private Integer rewards;

    private BigDecimal amt;

    private String debitCredit;

    private String transactionCategorization;

    public Long getId() {
        return id;
    }

    public Long getStatementFileId() {
        return statementFileId;
    }

    public String getTxnType() {
        return txnType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getTxnDateTime() {
        return txnDateTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRewards() {
        return rewards;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public String getTransactionCategorization() {
        return transactionCategorization;
    }
}
