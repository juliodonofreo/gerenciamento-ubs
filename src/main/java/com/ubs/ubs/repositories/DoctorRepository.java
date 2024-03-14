package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long>{

    Optional<Doctor> findByEmail(String email);
}
