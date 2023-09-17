package org.example.com.exception;

/**
 * Exception thrown when an invalid amount is provided for an operation.
 *
 * @author Natalie Werch
 */
public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
