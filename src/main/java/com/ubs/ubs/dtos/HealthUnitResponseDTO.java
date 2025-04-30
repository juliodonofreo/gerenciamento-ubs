package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.HealthUnit;

public record HealthUnitResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        String address
) {
    public HealthUnitResponseDTO(HealthUnit unit) {
        this(unit.getId(), unit.getName(), unit.getEmail(), unit.getPhone(), unit.getAddress());
    }
}