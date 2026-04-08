package com.kym.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "account_statement_structure")
public class AccountStatementStructure {
    @Id
    private Long id;
    private long statementFileId;
    private Integer headerRowIndex;
    private Integer dateColIndex;
    private Integer narrationColIndex;
    private Integer debitColIndex;
    private Integer creditColIndex;
    private Integer balanceColIndex;
    private Integer dataStartRowIndex;
    private Integer dataEndRowIndex;

    public AccountStatementStructure(long statementFileId,
                                     Integer headerRowIndex,
                                     Integer dateColIndex,
                                     Integer narrationColIndex,
                                     Integer debitColIndex,
                                     Integer creditColIndex,
                                     Integer balanceColIndex,
                                     Integer dataStartRowIndex,
                                     Integer dataEndRowIndex) {
        this.statementFileId = statementFileId;
        this.headerRowIndex = headerRowIndex;
        this.dateColIndex = dateColIndex;
        this.narrationColIndex = narrationColIndex;
        this.debitColIndex = debitColIndex;
        this.creditColIndex = creditColIndex;
        this.balanceColIndex = balanceColIndex;
        this.dataStartRowIndex = dataStartRowIndex;
        this.dataEndRowIndex = dataEndRowIndex;
    }

    public long getStatementFileId() {
        return statementFileId;
    }

    public Integer getHeaderRowIndex() {
        return headerRowIndex;
    }

    public Integer getDateColIndex() {
        return dateColIndex;
    }

    public Integer getNarrationColIndex() {
        return narrationColIndex;
    }

    public Integer getDebitColIndex() {
        return debitColIndex;
    }

    public Integer getCreditColIndex() {
        return creditColIndex;
    }

    public Integer getBalanceColIndex() {
        return balanceColIndex;
    }

    public Integer getDataStartRowIndex() {
        return dataStartRowIndex;
    }

    public Integer getDataEndRowIndex() {
        return dataEndRowIndex;
    }
}
