package org.example.com.exception;

/**
 * Exception thrown when a transaction cannot be found.
 *
 * @author Natalie Werch
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
