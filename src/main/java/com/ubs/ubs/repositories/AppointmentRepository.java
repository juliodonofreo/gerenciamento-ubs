package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.date BETWEEN :start AND :end")
    List<Appointment> findByDateBetween(Instant start, Instant end);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.doctor.healthUnit.id = :healthUnitId " +
            "OR a.patient.healthUnit.id = :healthUnitId")
    List<Appointment> findByDoctorHealthUnitOrPatientHealthUnit(@Param("healthUnitId") Long healthUnitId);

    List<Appointment> findByDoctorId(Long doctorId);
}