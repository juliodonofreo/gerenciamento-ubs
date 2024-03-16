package com.ubs.ubs.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CustomValidationError extends CustomError{

    private List<ErrorMessage> errors = new ArrayList<>();


    public CustomValidationError(Instant timestamp, Integer status, String message, String path) {
        super(timestamp, status, message, path);
    }

    public void addError(String field, String message){
        errors.add(new ErrorMessage(field, message));
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
