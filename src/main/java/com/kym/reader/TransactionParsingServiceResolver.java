package com.kym.reader;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionParsingServiceResolver {
    private final Map<String, TransactionParsingService> serviceMap;

    public TransactionParsingServiceResolver(List<TransactionParsingService> implementations) {
        this.serviceMap = implementations.stream()
                .collect(Collectors.toMap(TransactionParsingService::getType, Function.identity()));
    }

    public TransactionParsingService resolve(String type){
        TransactionParsingService transactionParsingService = serviceMap.get(type);
        if (transactionParsingService==null) throw new IllegalArgumentException("Invalid type passed: "+type);
        return transactionParsingService;
    }
}
