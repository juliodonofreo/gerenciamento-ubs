package com.ubs.ubs.services.exceptions;

public class CustomInvalidBodyException extends RuntimeException{
    private String message;

    public CustomInvalidBodyException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
