package com.ubs.ubs.dtos;

import jakarta.validation.constraints.Size;

public record StaffUpdateDTO(
        @Size(min = 5, max = 100) String name
) {}