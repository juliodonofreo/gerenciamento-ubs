package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PatientGetMinDTO extends UserGetDTO{
    private String cpf;
    private Instant birth_date;


    public PatientGetMinDTO() {
    }

    public PatientGetMinDTO(String name, String email, String cpf, Instant birth_date) {
        super(name, email);
        this.cpf = cpf;
        this.birth_date = birth_date;

    }

    public PatientGetMinDTO(Patient entity) {
        super(entity.getName(), entity.getEmail());
        this.cpf = entity.getCpf();
        this.birth_date = entity.getBirth_date();
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }
}
