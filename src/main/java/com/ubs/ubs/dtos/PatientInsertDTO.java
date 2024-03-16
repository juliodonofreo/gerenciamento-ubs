package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

import java.time.Instant;

public class PatientInsertDTO extends UserInsertDTO {

    @CPF(message = "CPF inválido.")
    private String cpf;

    @PastOrPresent(message = "A data não pode ser no futuro.")
    private Instant birth_date;

    public PatientInsertDTO() {
    }

    public PatientInsertDTO(Long id, String nome, String email, String password, String cpf, Instant birth_date) {
        super(id, nome, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
    }

    public PatientInsertDTO(Patient entity) {
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
