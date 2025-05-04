package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.Patient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

import java.time.Instant;

public class PatientInsertDTO extends UserInsertDTO {

    @CPF(message = ValidationErrorMessages.INVALID_CPF)
    @NotBlank(message = ValidationErrorMessages.MANDATORY_FIELD)
    private String cpf;

    @PastOrPresent(message = ValidationErrorMessages.DATE_CANNOT_BE_IN_FUTURE)
    @NotNull(message = ValidationErrorMessages.MANDATORY_FIELD)
    private Instant birth_date;

    private String address;

    public PatientInsertDTO() {
    }

    public PatientInsertDTO(Long id, String nome, String email, String password, String cpf, Instant birth_date, String address) {
        super(id, nome, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
        this.address = address;
    }

    public PatientInsertDTO(Patient entity) {
        super(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
        cpf = entity.getCpf();
        birth_date = entity.getBirth_date();
        address = entity.getAddress();
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public String getAddress() {
        return address;
    }
}
