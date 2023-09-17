package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing the status of a client.
 *
 * @author Natalie Werch
 */
@Getter
public enum ClientStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CLOSED("Closed"),
    VIP("VIP"),
    BLOCKED("Blocked"),
    ARCHIVED("Archived");

    private final String value;

    ClientStatus(String value) {
        this.value = value;
    }
}
