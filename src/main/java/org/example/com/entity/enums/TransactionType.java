package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the type of transaction.
 *
 * @author Natalie Werch
 */
@Getter
public enum TransactionType {

    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    PAYMENT("Payment");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }
}
