package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.Staff;

import java.util.List;

public record StaffResponseDTO(
        Long id,
        String name,
        String email,
        HealthUnitDTO healthUnit,
        List<RoleDTO> roles
) {
    public StaffResponseDTO(Staff staff) {
        this(
                staff.getId(),
                staff.getName(),
                staff.getEmail(),
                new HealthUnitDTO(staff.getHealthUnit()),
                staff.getRoles().stream().map(RoleDTO::new).toList()
        );
    }

    public record HealthUnitDTO(Long id, String name) {
        public HealthUnitDTO(HealthUnit unit) {
            this(unit.getId(), unit.getName());
        }
    }
}