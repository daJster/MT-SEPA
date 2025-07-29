package com.sepa.payment.system.service;

import com.sepa.payment.system.entity.Operation;
import com.sepa.payment.system.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class MTService implements TransactionService {

    public Boolean ProcessTransaction(Transaction t){ return true;};

    public Boolean verifyTransaction(Transaction t){ return true;};

    public Boolean ValidateTransaction(Transaction t){ return true;};

    public Boolean ProcessOperation(Operation o){ return true;};
}
