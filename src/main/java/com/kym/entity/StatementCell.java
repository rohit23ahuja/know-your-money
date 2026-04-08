package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="statement_cell")
public class StatementCell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long statementFileId;
    private Integer rowIndex;
    private Integer colIndex;
    private String cellRef;
    private String rawValueText;

    public StatementCell(long statementFileId, int rowIndex, int colIndex, String cellRef, String rawValueText) {
        this.statementFileId = statementFileId;
        this.rowIndex=rowIndex;
        this.colIndex =colIndex;
        this.cellRef=cellRef;
        this.rawValueText=rawValueText;
    }

    public Long getId() {
        return id;
    }

    public long getStatementFileId() {
        return statementFileId;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public String getCellRef() {
        return cellRef;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public String getRawValueText() {
        return rawValueText;
    }
}
