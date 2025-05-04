package com.ubs.ubs.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PatientCreateDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String cpf,
        @NotNull Instant birth_date,
        @NotNull Long healthUnitId,
        String address,
        String phone
) {}