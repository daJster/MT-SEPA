package com.sepa.payment.system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "id")
    private Account account;

    private long amount;

    private boolean deduct;

    public Operation(Account account, long amount, boolean deduct) {
        this.account = account;
        this.amount = amount;
        this.deduct = deduct;
    }
}
