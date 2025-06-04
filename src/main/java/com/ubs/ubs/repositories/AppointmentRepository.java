package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.HealthUnit;
import com.ubs.ubs.entities.enums.AppointmentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.date BETWEEN :start AND :end")
    List<Appointment> findByDateBetween(Instant start, Instant end);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.doctor.healthUnit.id = :healthUnitId " +
            "OR a.patient.healthUnit.id = :healthUnitId")
    List<Appointment> findByDoctorHealthUnitOrPatientHealthUnit(@Param("healthUnitId") Long healthUnitId);


    @Query("SELECT a FROM Appointment a " +
            "WHERE (a.doctor.healthUnit.id = :healthUnitId OR a.patient.healthUnit.id = :healthUnitId) " +
            "AND a.date >= :startInstant AND a.date < :endInstantPlusOneDay " + // Usar < endInstantPlusOneDay para incluir todo o dia final
            "AND a.state <> :cancelledStatus")
    List<Appointment> findByHealthUnitIdAndDateBetweenAndStatusNot(
            @Param("healthUnitId") Long healthUnitId,
            @Param("startInstant") Instant startInstant,
            @Param("endInstantPlusOneDay") Instant endInstantPlusOneDay, // Fim do intervalo (exclusive)
            @Param("cancelledStatus") AppointmentState cancelledStatus
    );


    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    @Query("SELECT a FROM Appointment a " +
            "WHERE (a.doctor.healthUnit.id = :healthUnitId OR a.patient.healthUnit.id = :healthUnitId) " +
            "AND a.date >= :startOfDayInstant AND a.date < :endOfDayInstant " +
            "AND a.state IN :statuses " +
            "ORDER BY a.date ASC") // a.date é Instant, então ordena por tempo
    List<Appointment> findByHealthUnitIdAndDayAndStatusesOrderByDate(
            @Param("healthUnitId") Long healthUnitId,
            @Param("startOfDayInstant") Instant startOfDayInstant,
            @Param("endOfDayInstant") Instant endOfDayInstant,
            @Param("statuses") List<AppointmentState> statuses
    );

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patient p " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH d.healthUnit hu " +
            "WHERE hu = :healthUnit " +
            "AND a.date BETWEEN :startDateTime AND :endDateTime " +
            "AND a.reminderSentAt IS NULL")
    List<Appointment> findAppointmentsForReminder(
            @Param("healthUnit") HealthUnit healthUnit,
            @Param("startDateTime") Instant startDateTime,
            @Param("endDateTime") Instant endDateTime);
}