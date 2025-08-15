package com.sepa.payment.system.repository.interfaces;

import com.sepa.payment.system.entity.Operation;
import com.sepa.payment.system.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.time.Instant;

// com.sepa.payment.system.repository.interfaces.TransactionRepository.java
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    boolean transactionAlreadyInDB(Transaction t);

//    List<Transaction> findByFromAcc(long fromAccId);
//
//    List<Transaction> findByToAcc(long toAccId);

}

