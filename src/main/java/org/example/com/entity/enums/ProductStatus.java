package org.example.com.entity.enums;

import lombok.Getter;

/**
 * Enumeration representing status of a product.
 *
 * @author Natalie Werch
 */
@Getter
public enum ProductStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    ProductStatus(String value) {
        this.value = value;
    }
}
