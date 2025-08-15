package com.sepa.payment.system.repository.interfaces;

import com.sepa.payment.system.entity.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// com.sepa.payment.system.repository.interfaces.AccountRepository.java
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
