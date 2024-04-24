package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.enums.AppointmentState;

import java.time.Instant;

public class AppointmentPatientGetDTO {

    private Long id;
    private Instant date;
    private String diagnosis;
    private AppointmentState state;

    private PatientGetMinDTO patient;

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

        this.patient = new PatientGetMinDTO(entity.getPatient());

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

    public PatientGetMinDTO getPatient() {
        return patient;
    }

    public AppointmentState getState() {
        return state;
    }
}
