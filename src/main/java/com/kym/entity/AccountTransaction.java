package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "account_transaction")
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long statementFileId;
    private LocalDate txnDate;
    private String narration;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private Integer sourceRowIndex;

    public AccountTransaction(long statementFileId,
                              LocalDate txnDate,
                              String narration,
                              BigDecimal debitAmount,
                              BigDecimal creditAmount,
                              BigDecimal balance,
                              Integer sourceRowIndex) {
        this.statementFileId = statementFileId;
        this.txnDate = txnDate;
        this.narration = narration;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.balance = balance;
        this.sourceRowIndex = sourceRowIndex;
    }

    public Long getId() {
        return id;
    }

    public long getStatementFileId() {
        return statementFileId;
    }

    public LocalDate getTxnDate() {
        return txnDate;
    }

    public String getNarration() {
        return narration;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Integer getSourceRowIndex() {
        return sourceRowIndex;
    }
}
