package com.sepa.payment.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "fromUUID is mandatory")
    private String fromAccUUID;

    @NotBlank(message = "toUUID is mandatory")
    private String toAccUUID;

    @Positive(message = "sent amount must be strictly positive")
    private long amount;

    @Column(name = "created_at", nullable = false)
    @FutureOrPresent(message = "date should be in present or future.")
    private Instant createdAt;
}
