package com.sepa.payment.system.service;

import com.sepa.payment.system.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

@Component
public class TransactionServiceFactory {

    private final Map<String, TransactionService> serviceMap = new HashMap<String, TransactionService>();

    @Autowired
    public TransactionServiceFactory(STService stService, MTService mtService) {
        setServiceMap(Arrays.asList(stService, mtService));
    }

    private void setServiceMap(List<TransactionService> services) {
        services.forEach(s -> serviceMap.put(s.getClass().getSimpleName(), s));
    }

    public TransactionService getST() {
        return serviceMap.get("STService");
    }

    public TransactionService getMT() {
        return serviceMap.get("MTService");
    }
}
