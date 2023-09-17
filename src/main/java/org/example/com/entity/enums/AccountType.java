package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the type of account.
 *
 * @author Natalie Werch
 */
@Getter
public enum AccountType {
    CREDIT("Credit"),
    DEPOSIT("Deposit");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }
}
