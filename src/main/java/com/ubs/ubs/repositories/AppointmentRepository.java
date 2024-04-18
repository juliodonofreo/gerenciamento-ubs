package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
