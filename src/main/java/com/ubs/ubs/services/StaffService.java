package com.ubs.ubs.services;

import com.ubs.ubs.dtos.StaffCreateDTO;
import com.ubs.ubs.dtos.StaffResponseDTO;
import com.ubs.ubs.dtos.StaffUpdateDTO;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.Staff;
import com.ubs.ubs.entities.Role;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.HealthUnitRepository;
import com.ubs.ubs.repositories.RoleRepository;
import com.ubs.ubs.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HealthUnitRepository healthUnitRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_UNIT')")
    public StaffResponseDTO create(StaffCreateDTO dto) {
        System.out.println("ID: " + dto.healthUnitId());
        HealthUnit healthUnit = healthUnitRepository.findById(dto.healthUnitId())
                .orElseThrow(() -> new RuntimeException("Health Unit not found"));

        // Verifica se a unidade pertence ao usuário autenticado
        if (!healthUnit.getId().equals(userService.getCurrentUser().getId())) {
            throw new AccessDeniedException("Unauthorized health unit assignment");
        }

        Staff staff = new Staff();
        staff.setName(dto.name());
        staff.setEmail(dto.email());
        staff.setPassword(passwordEncoder.encode(dto.password()));
        staff.setHealthUnit(healthUnit);

        staff.getRoles().add(roleRepository.findByAuthority("ROLE_STAFF").get());

        return new StaffResponseDTO(repository.save(staff));
    }

    @PreAuthorize("hasRole('ROLE_UNIT')")
    public StaffResponseDTO update(Long id, StaffUpdateDTO dto) {
        Staff staff = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (dto.name() != null) {
            staff.setName(dto.name());
        }

        return new StaffResponseDTO(repository.save(staff));
    }

    @PreAuthorize("hasRole('ROLE_UNIT')")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public List<StaffResponseDTO> findAll() {
        User user = userService.getCurrentUser();

        if (user.hasRole("ROLE_ADMIN")) {
            return repository.findAll().stream()
                    .map(StaffResponseDTO::new)
                    .toList();
        }

        return repository.findByHealthUnitId(user.getId()).stream()
                .map(StaffResponseDTO::new)
                .toList();
    }
}