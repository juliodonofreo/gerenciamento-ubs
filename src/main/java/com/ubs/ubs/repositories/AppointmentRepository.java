package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    public Appointment getAll(Long id);
}
