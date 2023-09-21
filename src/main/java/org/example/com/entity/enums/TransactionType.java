package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the type of transaction.
 *
 * @author Natalie Werch
 */
@Getter
public enum TransactionType {

    DEPOSIT,
    WITHDRAWAL,
    TRANSFER,
    PAYMENT
}
