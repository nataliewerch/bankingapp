package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing status of a manager.
 *
 * @author Natalie Werch
 */
@Getter
public enum ManagerStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    ManagerStatus(String value) {
        this.value = value;
    }
}
