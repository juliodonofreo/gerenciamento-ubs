package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Staff> findByHealthUnitId(Long healthUnitId);

}