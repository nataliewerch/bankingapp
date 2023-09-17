package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of an agreement.
 *
 * @author Natalie Werch
 */
@Getter
public enum AgreementStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PENDING("Pending"),
    TERMINATED("Terminated"),
    COMPLETED("Completed");

    private final String value;

    AgreementStatus(String value) {
        this.value = value;
    }
}
