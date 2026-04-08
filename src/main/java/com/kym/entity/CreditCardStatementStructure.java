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
    private Integer transactionTypeColIndex;
    private Integer customerNameColIndex;
    private Integer dateTimeColIndex;
    private Integer descriptionColIndex;
    private Integer rewardsColIndex;
    private Integer amtColIndex;
    private Integer debitCreditColIndex;
    private Integer dataStartRowIndex;
    private Integer dataEndRowIndex;

    public CreditCardStatementStructure(long statementFileId,
                                        Integer headerRowIndex,
                                        Integer transactionTypeColIndex,
                                        Integer customerNameColIndex,
                                        Integer dateTimeColIndex,
                                        Integer descriptionColIndex,
                                        Integer rewardsColIndex,
                                        Integer amtColIndex,
                                        Integer debitCreditColIndex,
                                        Integer dataStartRowIndex,
                                        Integer dataEndRowIndex) {
        this.statementFileId=statementFileId;
        this.headerRowIndex=headerRowIndex;
        this.transactionTypeColIndex=transactionTypeColIndex;
        this.customerNameColIndex=customerNameColIndex;
        this.dateTimeColIndex=dateTimeColIndex;
        this.descriptionColIndex=descriptionColIndex;
        this.rewardsColIndex=rewardsColIndex;
        this.amtColIndex=amtColIndex;
        this.debitCreditColIndex=debitCreditColIndex;
        this.dataStartRowIndex=dataStartRowIndex;
        this.dataEndRowIndex=dataEndRowIndex;
    }

    public long getStatementFileId() {
        return statementFileId;
    }

    public Integer getHeaderRowIndex() {
        return headerRowIndex;
    }

    public Integer getTransactionTypeColIndex() {
        return transactionTypeColIndex;
    }

    public Integer getCustomerNameColIndex() {
        return customerNameColIndex;
    }

    public Integer getDateTimeColIndex() {
        return dateTimeColIndex;
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

    public Integer getDebitCreditColIndex() {
        return debitCreditColIndex;
    }

    public Integer getDataStartRowIndex() {
        return dataStartRowIndex;
    }

    public Integer getDataEndRowIndex() {
        return dataEndRowIndex;
    }
}
