package org.example.com.exception;

/**
 * Exception thrown when an operation cannot be performed due to insufficient account balance.
 *
 * @author Natalie Werch
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
