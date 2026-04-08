package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "creditcard_transaction")
public class CreditCardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Integer sourceRowIndex;

    private String transactionCategorization;

    public CreditCardTransaction(){}

    public CreditCardTransaction(long statementFileId,
                                 String txnType,
                                 String customerName,
                                 LocalDateTime txnDateTime,
                                 String description,
                                 int rewards,
                                 BigDecimal amt,
                                 String debitCredit,
                                 Integer sourceRowIndex) {
        this.statementFileId=statementFileId;
        this.txnType=txnType;
        this.customerName=customerName;
        this.txnDateTime=txnDateTime;
        this.description=description;
        this.rewards=rewards;
        this.amt=amt;
        this.debitCredit=debitCredit;
        this.sourceRowIndex=sourceRowIndex;
    }

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
