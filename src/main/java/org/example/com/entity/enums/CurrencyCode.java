package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing currency codes.
 *
 * @author Natalie Werch
 */
@Getter
public enum CurrencyCode {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP");

    private final String value;

    CurrencyCode(String value) {
        this.value = value;
    }
}
