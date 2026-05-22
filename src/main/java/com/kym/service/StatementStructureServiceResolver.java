package com.kym.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StatementStructureServiceResolver {
    private final Map<String, StatementStructureService> serviceMap;

    public StatementStructureServiceResolver(List<StatementStructureService> implementations) {
        this.serviceMap = implementations.stream()
                .collect(Collectors.toMap(StatementStructureService::getType, Function.identity()));
    }

    public StatementStructureService resolve(String statementType) {
        StatementStructureService statementStructureService = serviceMap.get(statementType);
        if (statementStructureService == null) throw new IllegalArgumentException("Invalid statementType: "+statementType);
        return statementStructureService;
    }
}
