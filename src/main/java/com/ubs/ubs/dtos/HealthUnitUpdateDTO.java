package com.ubs.ubs.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HealthUnitUpdateDTO(
        @Size(min = 5, max = 100, message = "Nome deve ter entre 5 e 100 caracteres")
        String name,

        @Email(message = "Email deve ser válido")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String phone,

        @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
        String address,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password
) {}