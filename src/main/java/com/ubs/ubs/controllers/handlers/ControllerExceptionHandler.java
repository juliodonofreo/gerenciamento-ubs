package com.ubs.ubs.controllers.handlers;

import com.ubs.ubs.dtos.CustomError;
import com.ubs.ubs.services.exceptions.CustomInvalidBodyException;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<CustomError> notFound(CustomNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getServletPath());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CustomInvalidBodyException.class)
    public ResponseEntity<CustomError> invalidBody(CustomInvalidBodyException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getServletPath());
        return ResponseEntity.status(status).body(error);
    }
}
