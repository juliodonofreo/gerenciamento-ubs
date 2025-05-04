package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.enums.Specialization;

import java.util.List;
import java.util.stream.Collectors;

public record DoctorResponseDTO(
        Long id,
        String name,
        String email,
        Specialization specialization,
        HealthUnitDTO healthUnit,
        String crm,
        List<String> roles
) {
    public DoctorResponseDTO(Doctor doctor) {
        this(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getSpecialization(),
                doctor.getHealthUnit() != null ? new HealthUnitDTO(doctor.getHealthUnit()) : null,
                doctor.getCrm(),
                doctor.getRoles().stream()
                        .map(role -> role.getAuthority())
                        .collect(Collectors.toList())
        );
    }

    public record HealthUnitDTO(Long id, String name) {
        public HealthUnitDTO(HealthUnit healthUnit) {
            this(healthUnit.getId(), healthUnit.getName());
        }
    }
}