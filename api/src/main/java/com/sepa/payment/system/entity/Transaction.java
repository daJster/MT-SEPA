package com.sepa.payment.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "fromAcc is mandatory")
    @OneToOne
    @JoinColumn(name = "id")
    private Account fromAcc;

    @NotNull(message = "toAcc is mandatory")
    @OneToOne
    @JoinColumn(name = "id")
    private Account toAcc;

    @Positive(message = "sent amount must be strictly positive")
    private long amount;

    @Column(name = "created_at", nullable = false)
    @FutureOrPresent(message = "date should be in present or future.")
    private Instant createdAt;
}
