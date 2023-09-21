package org.example.com.exception;

/**
 * Exception thrown when an account is not active.
 *
 * @author Natalie Werch
 */
public class AccountInactiveException extends RuntimeException{

    public AccountInactiveException(String message) {
        super(message);
    }
}
