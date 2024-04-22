package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Dependent;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

public class DependentGetDTO {

    private Long id;
    private String name;
    private Instant birth_date;

    public DependentGetDTO(){

    }

    public DependentGetDTO(Long id, String name, Instant birth_date) {
        this.id = id;
        this.name = name;
        this.birth_date = birth_date;
    }

    public DependentGetDTO(Dependent entity){
        id = entity.getId();
        name = entity.getName();
        birth_date = entity.getBirth_date();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getBirth_date() {
        return birth_date;
    }
}
