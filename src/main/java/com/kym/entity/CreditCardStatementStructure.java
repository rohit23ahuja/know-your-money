package com.kym.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "creditcard_statement_structure")
public class CreditCardStatementStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long statementFileId;
    private Integer headerRowIndex;
    private Integer transactiontypeColIndex;
    private Integer customernameColIndex;
    private Integer datetimeColIndex;
    private Integer descriptionColIndex;
    private Integer rewardsColIndex;
    private Integer amtColIndex;
    private Integer debitcreditColIndex;
    private Integer dataStartRowIndex;
    private Integer dataEndRowIndex;

    public CreditCardStatementStructure(long statementFileId,
                                        Integer headerRowIndex,
                                        Integer transactiontypeColIndex,
                                        Integer customernameColIndex,
                                        Integer dateTimeColIndex,
                                        Integer descriptionColIndex,
                                        Integer rewardsColIndex,
                                        Integer amtColIndex,
                                        Integer debitcreditColIndex,
                                        Integer dataStartRowIndex,
                                        Integer dataEndRowIndex) {
        this.statementFileId=statementFileId;
        this.headerRowIndex=headerRowIndex;
        this.transactiontypeColIndex = transactiontypeColIndex;
        this.customernameColIndex = customernameColIndex;
        this.datetimeColIndex =dateTimeColIndex;
        this.descriptionColIndex=descriptionColIndex;
        this.rewardsColIndex=rewardsColIndex;
        this.amtColIndex=amtColIndex;
        this.debitcreditColIndex = debitcreditColIndex;
        this.dataStartRowIndex=dataStartRowIndex;
        this.dataEndRowIndex=dataEndRowIndex;
    }

    public long getStatementFileId() {
        return statementFileId;
    }

    public Integer getHeaderRowIndex() {
        return headerRowIndex;
    }

    public Integer getTransactiontypeColIndex() {
        return transactiontypeColIndex;
    }

    public Integer getCustomernameColIndex() {
        return customernameColIndex;
    }

    public Integer getDatetimeColIndex() {
        return datetimeColIndex;
    }

    public Integer getDescriptionColIndex() {
        return descriptionColIndex;
    }

    public Integer getRewardsColIndex() {
        return rewardsColIndex;
    }

    public Integer getAmtColIndex() {
        return amtColIndex;
    }

    public Integer getDebitcreditColIndex() {
        return debitcreditColIndex;
    }

    public Integer getDataStartRowIndex() {
        return dataStartRowIndex;
    }

    public Integer getDataEndRowIndex() {
        return dataEndRowIndex;
    }
    public Long getId() {
        return id;
    }
}
