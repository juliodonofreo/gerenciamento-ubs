package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.date BETWEEN :start AND :end")
    List<Appointment> findByDateBetween(Instant start, Instant end);
}