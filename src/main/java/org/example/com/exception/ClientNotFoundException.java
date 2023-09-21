package org.example.com.exception;

/**
 * Exception thrown when a client cannot be found.
 *
 * @author Natalie Werch
 */
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
