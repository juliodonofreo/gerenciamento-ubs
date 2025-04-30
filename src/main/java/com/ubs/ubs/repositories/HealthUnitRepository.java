package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.HealthUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthUnitRepository extends JpaRepository<HealthUnit, Long> {
    Optional<HealthUnit> findByEmail(String email);
    boolean existsByEmail(String email);
}