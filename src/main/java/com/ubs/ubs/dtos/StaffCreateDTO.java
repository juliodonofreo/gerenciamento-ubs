package com.ubs.ubs.dtos;

import jakarta.validation.constraints.*;

public record StaffCreateDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Long healthUnitId
) {}