package com.kym.detector;

import com.kym.entity.StatementCell;
import com.kym.entity.StatementDetail;
import com.kym.util.PropertyUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatementDetailDetector {
    private final PropertyUtils propertyUtils;

    public StatementDetailDetector(PropertyUtils propertyUtils) {
        this.propertyUtils = propertyUtils;
    }

    public StatementDetail detect(Long statementFileId, List<StatementCell> statementCells) {
        StatementCell accountNoStatementCell = statementCells.stream()
                .filter(statementCell -> statementCell.getRawValueText().contains("Credit Card No.:"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Account Number cell not found for statementFileId: " + statementFileId));
        String accountNumber = accountNoStatementCell.getRawValueText().substring(accountNoStatementCell.getRawValueText().length() - 5);
        String accountName = propertyUtils.getPropertiesMap().get(String.join(".", accountNumber, "accountname"));
        String statementType = propertyUtils.getPropertiesMap().get(String.join(".", accountNumber, "statementtype"));
        String bankCode = propertyUtils.getPropertiesMap().get(String.join(".", accountNumber, "bankcode"));

        return new StatementDetail(statementFileId,
                accountName,
                accountNumber,
                statementType,
                bankCode);
    }
}
