package com.sepa.payment.system.repository.interfaces;

import com.sepa.payment.system.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// com.sepa.payment.system.repository.interfaces.OperationRepository.java
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
