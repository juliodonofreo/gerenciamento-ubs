package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;

public class PatientGetDTO extends UserGetDTO{
    private String cpf;
    private Instant birth_date;

    public PatientGetDTO() {
    }

    public PatientGetDTO(String name, String email, String cpf, Instant birth_date) {
        super(name, email);
        this.cpf = cpf;
        this.birth_date = birth_date;
    }

    public PatientGetDTO(Patient entity) {
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
