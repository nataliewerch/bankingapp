package org.example.com.entity.enums;

public enum AccountType {
    CREDIT("Credit"),
    DEPOSIT("Deposit");

    private String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
