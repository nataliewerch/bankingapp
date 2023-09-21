package org.example.com.exception;

/**
 * Exception thrown when an agreement already exists for a specific account and product.
 *
 * @author Natalie Werch
 */
public class AgreementAlreadyExistsException extends RuntimeException{

    public AgreementAlreadyExistsException(String message) {
        super(message);
    }
}
