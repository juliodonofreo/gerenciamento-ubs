package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.enums.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Specialization specialization,
        @NotNull Long healthUnitId
) {}