package com.sepa.payment.system.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionServiceFactory {

    private final Map<String, TransactionService> serviceMap = new HashMap<String, TransactionService>();

    public TransactionServiceFactory() {
        serviceMap.put("STService", new STService());
        serviceMap.put("MTService", new MTService());
    }

    public TransactionService getST() {
        return serviceMap.get("STService");
    }

    public TransactionService getMT() {
        return serviceMap.get("MTService");
    }
}
