package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.AppointmentState;

import java.time.Instant;

public class AppointmentPatientGetDTO {

    private Long id;
    private Instant date;
    private String diagnosis;
    private AppointmentState state;

    private DoctorGetMinDTO doctor;

    public AppointmentPatientGetDTO(Long id, Instant date, String diagnosis, AppointmentState state) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.state = state;
    }

    public AppointmentPatientGetDTO(Appointment entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();
        this.state = entity.getState();

        this.doctor = new DoctorGetMinDTO(entity.getDoctor());

    }

    public Long getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public DoctorGetMinDTO getDoctor() {
        return doctor;
    }

    public AppointmentState getState() {
        return state;
    }
}
