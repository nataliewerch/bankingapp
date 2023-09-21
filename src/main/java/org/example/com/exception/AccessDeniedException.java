package org.example.com.exception;

/**
 * Exception thrown when access to operation is denied.
 *
 * @author Natalie Werch
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
