package org.example.com.exception;

/**
 * Exception thrown when attempting to delete a manager who still has products assigned to them.
 *
 * @author Natalie Werch
 */
public class ManagerHasProductsException extends RuntimeException{
    public ManagerHasProductsException(String message) {
        super(message);
    }
}
