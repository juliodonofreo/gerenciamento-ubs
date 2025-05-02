package com.ubs.ubs.services;

import com.ubs.ubs.dtos.DoctorCreateDTO;
import com.ubs.ubs.dtos.DoctorResponseDTO;
import com.ubs.ubs.dtos.DoctorUpdateDTO;
import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.DoctorRepository;
import com.ubs.ubs.repositories.HealthUnitRepository;
import com.ubs.ubs.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HealthUnitRepository healthUnitRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_UNIT')")
    @Transactional
    public DoctorResponseDTO create(DoctorCreateDTO dto) {
        HealthUnit healthUnit = getAuthorizedHealthUnit(dto.healthUnitId());

        if (doctorRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        Doctor doctor = new Doctor();
        doctor.setName(dto.name());
        doctor.setEmail(dto.email());
        doctor.setPassword(passwordEncoder.encode(dto.password()));
        doctor.setSpecialization(dto.specialization());
        doctor.setHealthUnit(healthUnit);
        doctor.getRoles().add(roleRepository.findByAuthority("ROLE_DOCTOR").get());

        return new DoctorResponseDTO(doctorRepository.save(doctor));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> findAll() {
        User user = userService.getCurrentUser();

        if (user.hasRole("ROLE_ADMIN")) {
            return doctorRepository.findAll().stream()
                    .map(DoctorResponseDTO::new)
                    .toList();
        }

        return doctorRepository.findByHealthUnitId(user.getId()).stream()
                .map(DoctorResponseDTO::new)
                .toList();
    }

    // Métodos auxiliares
    private HealthUnit getAuthorizedHealthUnit(Long unitId) {
        HealthUnit healthUnit = healthUnitRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("Health Unit not found"));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN") && !healthUnit.getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized health unit access");
        }

        return healthUnit;
    }

    @Transactional(readOnly = true)
    public DoctorResponseDTO findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN") && !user.hasRole("ROLE_UNIT")) {
            if (user.hasRole("ROLE_DOCTOR") && !doctor.getId().equals(user.getId())) {
                throw new AccessDeniedException("Doctors can only view their own profile");
            }
        }

        return new DoctorResponseDTO(doctor);
    }

    @Transactional
    public DoctorResponseDTO update(Long id, DoctorUpdateDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN")) {
            // Verifica se a unidade do médico pertence ao usuário
            if (user.hasRole("ROLE_UNIT") && !doctor.getHealthUnit().getId().equals(user.getId())) {
                throw new AccessDeniedException("Unauthorized to update this doctor");
            }
        }

        // Atualiza campos permitidos
        if (dto.getName() != null) {
            doctor.setName(dto.getName());
        }
        if (dto.getSpecialization() != null) {
            doctor.setSpecialization(dto.getSpecialization());
        }
        if (dto.getHealthUnitId() != null) {
            HealthUnit newUnit = healthUnitRepository.findById(dto.getHealthUnitId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Unit not found"));
            doctor.setHealthUnit(newUnit);
        }

        return new DoctorResponseDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN")) {
            if (user.hasRole("ROLE_UNIT") && !doctor.getHealthUnit().getId().equals(user.getId())) {
                throw new AccessDeniedException("Unauthorized to delete this doctor");
            }
        }

        // Remove relacionamentos
        doctor.getAppointments().forEach(appointment -> appointment.setDoctor(null));

        doctorRepository.delete(doctor);
    }
}