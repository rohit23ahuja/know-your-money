package com.kym.specification;

import com.kym.api.TransactionSearchRequest;
import com.kym.entity.CreditCardTransaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class CreditCardTransactionSpecification {

    public static Specification<CreditCardTransaction> withFilters(TransactionSearchRequest transactionSearchRequest) {
        return Specification
                .where(hasStatementFileId(transactionSearchRequest.statementFileId()))
                .and(hasAccountHolder(transactionSearchRequest.accountHolder()))
                .and(hasDescription(transactionSearchRequest.description()))
                .and(hasCategory(transactionSearchRequest.category()))
                .and(hasTransactionDateBetween(transactionSearchRequest.transactionDateFrom(), transactionSearchRequest.transactionDateTo()));
    }

    private static Specification<CreditCardTransaction> hasTransactionDateBetween(LocalDate transactionDateFrom, LocalDate transactionDateTo) {
        return (root, query, criteriaBuilder) -> {
            if (transactionDateFrom != null && transactionDateTo != null)
                return criteriaBuilder.between(root.get("txnDateTime"), transactionDateFrom   , transactionDateTo);
            return null;
        };
    }

    private static Specification<CreditCardTransaction> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
            StringUtils.hasText(category)
                    ? criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionCategorization")), "%"+category.toLowerCase()+"%")
                    : null;
    }

    private static Specification<CreditCardTransaction> hasDescription(String description) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(description)
                        ? criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%"+description.toLowerCase()+"%")
                        : null;
    }

    private static Specification<CreditCardTransaction> hasAccountHolder(String accountHolder) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(accountHolder)
                        ? criteriaBuilder.equal(root.get("customerName"), accountHolder)
                        : null;
    }

    private static Specification<CreditCardTransaction> hasStatementFileId(Long statementFileId) {
        return (root, query, criteriaBuilder) ->
                statementFileId != null
                        ? criteriaBuilder.equal(root.get("statementFileId"), statementFileId)
                        : null;
    }

}
