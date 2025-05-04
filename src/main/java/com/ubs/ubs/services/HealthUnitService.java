package com.ubs.ubs.services;

import com.ubs.ubs.dtos.HealthUnitCreateDTO;
import com.ubs.ubs.dtos.HealthUnitResponseDTO;
import com.ubs.ubs.dtos.HealthUnitUpdateDTO;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.repositories.HealthUnitRepository;
import com.ubs.ubs.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthUnitService {

    @Autowired
    private HealthUnitRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HealthUnitResponseDTO create(HealthUnitCreateDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        HealthUnit unit = new HealthUnit();
        unit.setName(dto.name());
        unit.setEmail(dto.email());
        unit.setPassword(passwordEncoder.encode(dto.password()));
        unit.setPhone(dto.phone());
        unit.setAddress(dto.address());

        Role unitRole = roleRepository.findByAuthority("ROLE_UNIT").get();
        unit.getRoles().add(unitRole);

        HealthUnit savedUnit = repository.save(unit);
        return convertToDTO(savedUnit);
    }

    @PreAuthorize("permitAll()")
    public List<HealthUnitResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HealthUnitResponseDTO update(Long id, HealthUnitUpdateDTO dto) {
        HealthUnit unit = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada"));

        // Verifica se o novo email já está em uso por outra unidade
        if (dto.email() != null && !unit.getEmail().equals(dto.email())) {
            if (repository.existsByEmail(dto.email())) {
                throw new IllegalArgumentException("Email já está em uso");
            }
            unit.setEmail(dto.email());
        }

        if (dto.name() != null) unit.setName(dto.name());
        if (dto.phone() != null) unit.setPhone(dto.phone());
        if (dto.address() != null) unit.setAddress(dto.address());

        // Atualiza a senha apenas se for fornecida
        if (dto.password() != null && !dto.password().isBlank()) {
            unit.setPassword(passwordEncoder.encode(dto.password()));
        }

        HealthUnit updatedUnit = repository.save(unit);
        return convertToDTO(updatedUnit);
    }

    private HealthUnitResponseDTO convertToDTO(HealthUnit unit) {
        return new HealthUnitResponseDTO(
                unit.getId(),
                unit.getName(),
                unit.getEmail(),
                unit.getPhone(),
                unit.getAddress()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Unidade de saúde não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    public HealthUnitResponseDTO findById(Long id) {
        HealthUnit unit = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de saúde não encontrada com ID: " + id));
        return convertToDTO(unit);
    }

}