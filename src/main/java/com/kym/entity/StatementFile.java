package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "statement_file")
public class StatementFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountName;
    private String accountNumber;
    private String statementType;
    private String bankCode;
    private String originalFilename;

    public StatementFile(String accountName, String accountNumber, String statementType, String bankCode, String originalFilename) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.statementType = statementType;
        this.bankCode = bankCode;
        this.originalFilename = originalFilename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
}
