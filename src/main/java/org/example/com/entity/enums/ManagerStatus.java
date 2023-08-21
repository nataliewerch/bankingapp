package org.example.com.entity.enums;

public enum ManagerStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    ManagerStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
