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
    private String fileName;
    private String statementMonthYear;
    private String statementType;

    public StatementFile() {

    }
    public StatementFile(String fileName, String statementMonthYear, String statementType) {
        this.fileName = fileName;
        this.statementMonthYear = statementMonthYear;
        this.statementType = statementType;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatementMonthYear() {
        return statementMonthYear;
    }

    public String getStatementType() {
        return statementType;
    }
}
