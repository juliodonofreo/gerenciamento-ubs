package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.Instant;

public class PatientUpdateDTO extends UserUpdateDTO{

    @CPF(message = "CPF inválido.")
    private String cpf;

    @PastOrPresent(message = "A data não pode ser no futuro.")
    private Instant birth_date;

    public PatientUpdateDTO() {
    }

    public PatientUpdateDTO(String nome, String email, String password, String cpf, Instant birth_date) {
        super(nome, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
    }

    public PatientUpdateDTO(Patient entity) {
        super(entity.getName(), entity.getEmail(), entity.getPassword());
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
