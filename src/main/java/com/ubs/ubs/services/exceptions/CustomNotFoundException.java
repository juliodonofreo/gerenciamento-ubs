package com.ubs.ubs.services.exceptions;

public class CustomNotFoundException extends RuntimeException{

    private String message;

    public CustomNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
