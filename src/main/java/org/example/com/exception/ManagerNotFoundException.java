package org.example.com.exception;

/**
 * Exception thrown when a manager cannot be found.
 *
 * @author Natalie Werch
 */
public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(String message) {
        super(message);
    }
}
