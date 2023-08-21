package org.example.com.entity.enums;

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

    public String getValue() {
        return value;
    }
}
