package org.example.com.exception;

/**
 * Exception thrown when attempting to delete a manager who still has clients assigned to them.
 *
 * @author Natalie Werch
 */
public class ManagerHasClientsException extends RuntimeException{

    public ManagerHasClientsException(String message) {
        super(message);
    }
}
