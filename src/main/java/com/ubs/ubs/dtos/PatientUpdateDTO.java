package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.Patient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;

import java.time.Instant;

public class PatientUpdateDTO extends UserUpdateDTO{

    @CPF(message = ValidationErrorMessages.INVALID_CPF)
    private String cpf;

    @PastOrPresent(message = ValidationErrorMessages.DATE_CANNOT_BE_IN_FUTURE)
    private Instant birth_date;

    @Valid
    private AddressUpdateDTO address;

    private Long healthUnitId;

    public PatientUpdateDTO() {
    }

    public PatientUpdateDTO(String nome, String email, String password, String cpf, Instant birth_date, Long healthUnitId) {
        super(nome, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
        this.healthUnitId = healthUnitId;
    }

    public PatientUpdateDTO(Patient entity) {
        super(entity.getName(), entity.getEmail(), entity.getPassword());
        cpf = entity.getCpf();
        birth_date = entity.getBirth_date();
        address = new AddressUpdateDTO(entity.getAddress());
        healthUnitId = entity.getHealthUnit().getId();
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public AddressUpdateDTO getAddress() {
        return address;
    }

    public Long getHealthUnitId() {
        return healthUnitId;
    }
}
