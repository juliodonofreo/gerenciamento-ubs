package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> getPatientByCpf(String cpf);
    boolean existsByCpf(String cpf);
    List<Patient> findByHealthUnitId(Long healthUnitId);

    boolean existsByEmail(String email);
}
