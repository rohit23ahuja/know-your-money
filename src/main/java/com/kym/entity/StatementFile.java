package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "statement_file")
public class StatementFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private LocalDate statementYearMonth;
    private String statementType;

    public StatementFile() {

    }
    public StatementFile(String fileName, LocalDate statementYearMonth, String statementType) {
        this.fileName = fileName;
        this.statementYearMonth = statementYearMonth;
        this.statementType = statementType;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDate getStatementYearMonth() {
        return statementYearMonth;
    }

    public String getStatementType() {
        return statementType;
    }
}
