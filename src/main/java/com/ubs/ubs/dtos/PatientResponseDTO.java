package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.DoctorResponseDTO;
import com.ubs.ubs.entities.Address;
import com.ubs.ubs.entities.Patient;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record PatientResponseDTO(
        Long id,
        String name,
        String email,
        String cpf,
        Instant birth_date,
        DoctorResponseDTO.HealthUnitDTO healthUnit,
        AddressDTO address,
        List<String> roles
) {
    public PatientResponseDTO(Patient patient) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getCpf(),
                patient.getBirth_date(),
                patient.getHealthUnit() != null ? new DoctorResponseDTO.HealthUnitDTO(patient.getHealthUnit()) : null,
                patient.getAddress() != null ? new AddressDTO(patient.getAddress()) : null,
                patient.getRoles().stream()
                        .map(role -> role.getAuthority())
                        .collect(Collectors.toList())
        );
    }

    public record AddressDTO(
            String street,
            Integer number,
            String district,
            String city,
            String cep
    ) {
        public AddressDTO(Address address) {
            this(
                    address.getStreet(),
                    address.getNumber(),
                    address.getDistrict(),
                    address.getCity(),
                    address.getCep()
            );
        }
    }
}