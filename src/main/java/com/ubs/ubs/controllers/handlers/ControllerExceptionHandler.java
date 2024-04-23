package com.ubs.ubs.controllers.handlers;

import com.ubs.ubs.dtos.CustomError;
import com.ubs.ubs.dtos.CustomValidationError;
import com.ubs.ubs.dtos.ErrorMessage;
import com.ubs.ubs.services.exceptions.CustomInvalidBodyException;
import com.ubs.ubs.services.exceptions.CustomNotFoundException;
import com.ubs.ubs.services.exceptions.CustomRepeatedException;
import com.ubs.ubs.services.exceptions.ForbiddenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> validation(MethodArgumentNotValidException e ,HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        CustomValidationError error = new CustomValidationError(Instant.now(), status.value(), "Campos inválidos", request.getServletPath());

        List<FieldError> valdationErrors = e.getFieldErrors();
        valdationErrors
                .stream()
                .forEach(x -> error.addError(x.getField(), x.getDefaultMessage()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CustomRepeatedException.class)
    public ResponseEntity<CustomError> repeated(CustomRepeatedException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomValidationError error = new CustomValidationError(Instant.now(), status.value(), "Erro de validação", request.getServletPath());

        List<ErrorMessage> valdationErrors = e.getErrors();
        valdationErrors
                .stream()
                .forEach(x -> error.addError(x.getField(), x.getMessage()));

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getServletPath());
        return ResponseEntity.status(status).body(error);
    }
}

