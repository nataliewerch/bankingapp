package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of an account.
 *
 * @author Natalie Werch
 */
@Getter
public enum AccountStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    FROZEN("Frozen");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }
}
