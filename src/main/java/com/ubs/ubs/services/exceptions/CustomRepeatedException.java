package com.ubs.ubs.services.exceptions;

public class CustomRepeatedException extends RuntimeException{

    String message;

    public CustomRepeatedException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
