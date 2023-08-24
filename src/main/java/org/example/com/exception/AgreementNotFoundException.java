package org.example.com.exception;

public class AgreementNotFoundException extends RuntimeException{

    public AgreementNotFoundException(String message) {
        super(message);
    }
}
