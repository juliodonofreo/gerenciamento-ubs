package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;

public class PatientDTO extends UserDTO {
    private String cpf;
    private Instant birth_date;

    public PatientDTO() {
    }

    public PatientDTO(Long id, String nome, String email, String password, String cpf, Instant birth_date) {
        super(id, nome, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
    }

    public PatientDTO(Patient entity) {
        super(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
        cpf = entity.getCpf();
        birth_date = entity.getBirth_date();
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }
}
