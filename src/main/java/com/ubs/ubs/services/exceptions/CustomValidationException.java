package com.ubs.ubs.services.exceptions;

public class CustomValidationException extends RuntimeException{

    private String message;

    public CustomValidationException(String message) {
        this.message = message;
    }
}
