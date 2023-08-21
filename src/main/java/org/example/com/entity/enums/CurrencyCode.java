package org.example.com.entity.enums;

public enum CurrencyCode {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP");

    private final String value;

    CurrencyCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
