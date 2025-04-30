package com.ubs.ubs.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HealthUnitCreateDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 5, max = 100, message = "Nome deve ter entre 5 e 100 caracteres")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String phone,

        @NotBlank(message = "Endereço é obrigatório")
        @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
        String address
) {}