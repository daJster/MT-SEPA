package com.sepa.payment.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table
public class Account {

    @Id
    String uuid;

    @NotBlank(message = "account needs to have a full name.")
    String fullName;

    @PositiveOrZero(message = "balance should be only positive or zero.")
    long balance;

    @PrePersist
    public void generateUUID() {
        this.uuid = UUID.randomUUID().toString();
    }
}
