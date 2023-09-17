package org.example.com.exception;

/**
 * Exception thrown when an account cannot be found.
 *
 * @author Natalie Werch
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
