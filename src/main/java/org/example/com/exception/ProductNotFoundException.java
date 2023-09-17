package org.example.com.exception;

/**
 * Exception thrown when a product cannot be found.
 *
 * @author Natalie Werch
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
