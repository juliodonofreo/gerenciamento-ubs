package com.ubs.ubs.services.exceptions;

import com.ubs.ubs.dtos.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

public class CustomRepeatedException extends RuntimeException{

    List<ErrorMessage> errors = new ArrayList<>();

    public CustomRepeatedException(){
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void addError(String field, String message){
        errors.add(new ErrorMessage(field, message));
    }
}
