package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.Schedule;
import com.ubs.ubs.entities.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDoctorId(Long doctorId);

    List<Schedule> findByDoctor(Doctor doctor);

    Optional<Schedule> findByDoctorAndDayOfWeekAndStartTimeAndEndTime(
            Doctor doctor, DayOfWeek dayOfWeek, java.time.LocalTime startTime, java.time.LocalTime endTime);
}