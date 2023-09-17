package org.example.com.exception;
/**
 * Exception thrown when attempting to create a user with a login that already exists in the system.
 *
 * @author Natalie Werch
 */
public class LoginAlreadyExistsException extends RuntimeException{
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
