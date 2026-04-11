package com.kym.entity;

import jakarta.persistence.*;

@Entity
@Table(name="statement_details")
public class StatementDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long statementFileId;
    private String accountName;
    private String accountNumber;
    private String statementType;
    private String bankCode;

    public StatementDetail(Long statementFileId, String accountName, String accountNumber, String statementType, String bankCode) {
        this.statementFileId = statementFileId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.statementType = statementType;
        this.bankCode = bankCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStatementType() {
        return statementType;
    }
    public String getBankCode() {
        return bankCode;
    }

    public Long getId() {
        return id;
    }
}
