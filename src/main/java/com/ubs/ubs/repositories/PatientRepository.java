package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> getPatientByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
