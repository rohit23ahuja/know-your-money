package com.kym.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="statement_cell")
public class StatementCell {
    @Id
    private Long id;
    private long statementFileId;
    private Integer rowIndex;
    private Integer columnIndex;
    private String cellRef;
    private String rawValueText;

    public StatementCell(long statementFileId, int rowIndex, int columnIndex, String cellRef, String rawValueText) {
        this.statementFileId = statementFileId;
        this.rowIndex=rowIndex;
        this.columnIndex=columnIndex;
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

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public String getRawValueText() {
        return rawValueText;
    }
}
