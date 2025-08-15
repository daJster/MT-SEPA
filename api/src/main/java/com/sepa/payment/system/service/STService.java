package com.sepa.payment.system.service;

import com.sepa.payment.system.entity.Account;
import com.sepa.payment.system.entity.Operation;
import com.sepa.payment.system.entity.Transaction;
import com.sepa.payment.system.repository.interfaces.AccountRepository;
import com.sepa.payment.system.repository.interfaces.OperationRepository;
import com.sepa.payment.system.repository.interfaces.TransactionRepository;
import com.sepa.payment.system.service.interfaces.TransactionService;

import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class STService implements TransactionService {

    private final RabbitTemplate rabbitTemplate;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final OperationRepository operationRepository;

    @Value("${rabbitmq.queue.transaction.name:transaction-processing-queue}")
    private String transactionQueue;

    @Value("${rabbitmq.exchange.name:transaction-exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:transaction.process}")
    private String routingKey;

    @Autowired
    public STService(RabbitTemplate rabbitTemplate,
                     AccountRepository accountRepository,
                     TransactionRepository transactionRepository,
                     OperationRepository operationRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    @RabbitListener(queues = "${rabbitmq.queue.transaction.name:transaction-processing-queue}")
    public Boolean ProcessTransaction(@NotNull Transaction t) {
        log.info("Processing transaction with ID: {}", t.getId());

        try {
            // Validate transaction first
            if (!ValidateTransaction(t)) {
                log.error("Transaction validation failed for transaction ID: {}", t.getId());
                return false;
            }

            // Verify transaction
            if (!verifyTransaction(t)) {
                log.error("Transaction verification failed for transaction ID: {}", t.getId());
                return false;
            }

            // Send transaction to RabbitMQ queue for processing
            rabbitTemplate.convertAndSend(exchangeName, routingKey, t);
            log.info("Transaction sent to queue successfully: {}", t.getId());

            // Create and process operations *sequentially*
            Operation debitOperation = new Operation(t.getFromAcc(), t.getAmount(), true);
            Operation creditOperation = new Operation(t.getToAcc(), t.getAmount(), false);

            Boolean debitResult = ProcessOperation(debitOperation);
            Boolean creditResult = ProcessOperation(creditOperation);

            if (debitResult && creditResult) {
                // Save transaction as processed
                transactionRepository.save(t);
                log.info("Transaction processed successfully: {}", t.getId());
                return true;
            } else {
                log.error("Operation processing failed for transaction: {}", t.getId());
                return false;
            }

        } catch (Exception e) {
            log.error("Error processing transaction {}: {}", t.getId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean verifyTransaction(@NotNull Transaction t) {
        log.debug("Verifying transaction: {}", t.getId());

        try {
            // Check if both accounts exist
            @NotNull Optional<Account> fromAccount = accountRepository.findById(t.getFromAcc().getId());
            @NotNull Optional<Account> toAccount = accountRepository.findById(t.getToAcc().getId());

            // Check if from account has sufficient balance
            if (fromAccount.isPresent() && fromAccount.get().getBalance() < t.getAmount()) {
                log.warn("Insufficient balance in account {}: has {}, needs {}",
                        t.getFromAcc(), fromAccount.get().getBalance(), t.getAmount());
                return false;
            }

            // Check for duplicate transactions (optional - based on business logic)
//            boolean isDuplicate = transactionRepository.transactionAlreadyInDB(t);
//
//            if (isDuplicate) {
//                log.warn("Duplicate transaction detected: {}", t);
//                return false;
//            }

            log.debug("Transaction verification successful: {}", t.getId());
            return true;

        } catch (Exception e) {
            log.error("Error verifying transaction {}: {}", t.getId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean ValidateTransaction(@NotNull Transaction t) {
        log.debug("Validating transaction: {}", t.getId());

        try {

            // Check mandatory fields
            if (t.getFromAcc() == null) {
                log.warn("FromAccUUID is null or empty");
                return false;
            }

            if (t.getToAcc() == null) {
                log.warn("ToAccUUID is null or empty");
                return false;
            }

            // Check if trying to transfer to the same account
            if (t.getFromAcc().equals(t.getToAcc())) {
                log.warn("Cannot transfer to the same account: {}", t.getFromAcc());
                return false;
            }

            // Check amount is positive
            if (t.getAmount() <= 0) {
                log.warn("Transaction amount must be positive: {}", t.getAmount());
                return false;
            }

            // Check created date
            if (t.getCreatedAt() == null) {
                log.warn("CreatedAt timestamp is null");
                return false;
            }


            log.debug("Transaction validation successful: {}", t.getId());
            return true;

        } catch (Exception e) {
            log.error("Error validating transaction {}: {}", t.getId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean ProcessOperation(@NotNull Operation o) {
        log.debug("Processing operation: {}", o.getId());

        try {
            // Validate operation
            if (o.getAccount() == null) {
                log.warn("Operation AccUUID is null or empty");
                return false;
            }

            if (o.getAmount() <= 0) {
                log.warn("Operation amount must be positive: {}", o.getAmount());
                return false;
            }

            // Get the account
            Optional<Account> account = accountRepository.findById(o.getAccount().getId());

            if (account.isEmpty()) { return false; }
            // Process the operation
            if (o.isDeduct()) {
                // Debit operation - subtract amount
                if (account.get().getBalance() < o.getAmount()) {
                    log.error("Insufficient balance for debit operation. Account: {}, Balance: {}, Amount: {}",
                            account.get().getId(), account.get().getBalance(), o.getAmount());
                    return false;
                }
                account.get().setBalance(account.get().getBalance() - o.getAmount());
            } else {
                // Credit operation - add amount
                account.get().setBalance(account.get().getBalance() + o.getAmount());
            }

            // Save updated account and operation
            accountRepository.save(account.get());
            operationRepository.save(o);

            log.info("Operation processed successfully: {} for account: {}, new balance: {}",
                    o.getId(), account.get().getId(), account.get().getBalance());

            return true;

        } catch (Exception e) {
            log.error("Error processing operation {}: {}", o.getId(), e.getMessage(), e);
            return false;
        }
    }
    
    // Helper method to validate UUID format
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}