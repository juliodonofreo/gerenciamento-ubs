package com.ubs.ubs.services;

import com.ubs.ubs.dtos.PatientCreateDTO;
import com.ubs.ubs.dtos.PatientResponseDTO;
import com.ubs.ubs.dtos.PatientUpdateDTO;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.Patient;
import com.ubs.ubs.entities.Staff;
import com.ubs.ubs.entities.User;
import com.ubs.ubs.repositories.HealthUnitRepository;
import com.ubs.ubs.repositories.PatientRepository;
import com.ubs.ubs.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private HealthUnitRepository healthUnitRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_UNIT', 'ROLE_STAFF')")
    @Transactional
    public PatientResponseDTO create(PatientCreateDTO dto) {
        HealthUnit healthUnit = validateHealthUnitAccess(dto.healthUnitId());

        if (patientRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Patient patient = new Patient();
        patient.setName(dto.name());
        patient.setEmail(dto.email());
        patient.setAddress(dto.address());
        patient.setPassword(passwordEncoder.encode(dto.password()));
        patient.setCpf(dto.cpf());
        patient.setBirth_date(dto.birth_date());
        patient.setHealthUnit(healthUnit);
        patient.setPhone(dto.phone());
        patient.getRoles().add(roleRepository.findByAuthority("ROLE_PATIENT").get());

        return new PatientResponseDTO(patientRepository.save(patient));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_STAFF')")
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> findAll() {
        User user = userService.getCurrentUser();

        if (user.hasRole("ROLE_ADMIN")) {
            return patientRepository.findAll().stream()
                    .map(PatientResponseDTO::new)
                    .toList();
        }

        return patientRepository.findByHealthUnitId(getEffectiveUnitId(user)).stream()
                .map(PatientResponseDTO::new)
                .toList();
    }

    private HealthUnit validateHealthUnitAccess(Long unitId) {
        HealthUnit healthUnit = healthUnitRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("Health Unit not found"));

        User user = userService.getCurrentUser();
        if (user.hasRole("ROLE_STAFF")) {
            Staff staff = (Staff) user;
            if (!staff.getHealthUnit().getId().equals(unitId)) {
                throw new AccessDeniedException("Staff not authorized for this health unit");
            }
        } else if (user.hasRole("ROLE_UNIT") && !user.getId().equals(unitId)) {
            throw new AccessDeniedException("Unauthorized health unit access");
        }

        return healthUnit;
    }

    private Long getEffectiveUnitId(User user) {
        if (user.hasRole("ROLE_STAFF")) {
            return ((Staff) user).getHealthUnit().getId();
        }
        return user.getId(); // Para unidades
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN") && !user.hasRole("ROLE_UNIT") && !user.hasRole("ROLE_STAFF")) {
            if (user.hasRole("ROLE_PATIENT") && !patient.getId().equals(user.getId())) {
                throw new AccessDeniedException("Patients can only view their own profile");
            }
        }

        return new PatientResponseDTO(patient);
    }

    @Transactional
    public PatientResponseDTO update(Long id, PatientUpdateDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        User user = userService.getCurrentUser();

        boolean isOwnProfile = user.getId().equals(patient.getId());

        if (!user.hasRole("ROLE_ADMIN") && !isOwnProfile) {
            if (user.hasRole("ROLE_STAFF")) {
                Staff staff = (Staff) user;
                if (!staff.getHealthUnit().getId().equals(patient.getHealthUnit().getId())) {
                    throw new AccessDeniedException("Unauthorized to update this patient");
                }
            }
            else if (user.hasRole("ROLE_UNIT") && !patient.getHealthUnit().getId().equals(user.getId())) {
                throw new AccessDeniedException("Unauthorized to update this patient");
            }
            else {
                throw new AccessDeniedException("Unauthorized to update this patient");
            }
        }

        // Atualiza campos permitidos
        if (dto.getName() != null) {
            patient.setName(dto.getName());
        }
        if (dto.getPassword() != null) {
            patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getBirth_date() != null) {
            patient.setBirth_date(dto.getBirth_date());
        }
        if(dto.getEmail() != null){
            patient.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            patient.setAddress(dto.getAddress());
        }
        if (dto.getPhone() != null) {
            patient.setPhone(dto.getPhone());
        }
        // Restringe atualização de unidade de saúde apenas para staff/admin
        if (dto.getHealthUnitId() != null && !isOwnProfile) {
            HealthUnit newUnit = healthUnitRepository.findById(dto.getHealthUnitId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Unit not found"));
            patient.setHealthUnit(newUnit);
        }

        return new PatientResponseDTO(patientRepository.save(patient));
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));

        User user = userService.getCurrentUser();
        if (!user.hasRole("ROLE_ADMIN")) {
            if (user.hasRole("ROLE_UNIT") && !patient.getHealthUnit().getId().equals(user.getId())) {
                throw new AccessDeniedException("Unauthorized to delete this patient");
            }
        }

        // Remove relacionamentos
        patient.getDependents().forEach(dependent -> dependent.setCompanion(null));
        patient.getAppointments().forEach(appointment -> appointment.setPatient(null));

        patientRepository.delete(patient);
    }
}