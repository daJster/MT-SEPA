package com.sepa.payment.system.service.interfaces;

import com.sepa.payment.system.entity.Operation;
import com.sepa.payment.system.entity.Transaction;

public interface TransactionService {

    public Boolean ProcessTransaction(Transaction t);

    public Boolean verifyTransaction(Transaction t);

    public Boolean ValidateTransaction(Transaction t);

    public Boolean ProcessOperation(Operation o);
}
