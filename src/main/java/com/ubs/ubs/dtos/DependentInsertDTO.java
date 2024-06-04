package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

public class DependentInsertDTO {

    private String name;

    @PastOrPresent(message = ValidationErrorMessages.DATE_CANNOT_BE_IN_FUTURE)
    private Instant birth_date;

    public DependentInsertDTO(){

    }

    public DependentInsertDTO(String name, Instant birth_date) {
        this.name = name;
        this.birth_date = birth_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Instant birth_date) {
        this.birth_date = birth_date;
    }
}
