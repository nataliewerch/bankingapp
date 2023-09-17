package org.example.com.exception;

/**
 * Exception thrown when an agreement cannot be found.
 *
 * @author Natalie Werch
 */
public class AgreementNotFoundException extends RuntimeException{

    public AgreementNotFoundException(String message) {
        super(message);
    }
}
