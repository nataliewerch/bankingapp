package org.example.com.entity.enums;

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

    public String getValue() {
        return value;
    }
}
