package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Appointment;

import java.time.Instant;

public class AppointmentGetDTO {

    private Long id;
    private Instant date;
    private String diagnosis;

    private DoctorGetDTO doctor;
    private PatientGetDTO patient;

    public AppointmentGetDTO(Long id, Instant date, String diagnosis) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
    }

    public AppointmentGetDTO(Appointment entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();

        this.patient = new PatientGetDTO(entity.getPatient());
        this.doctor = new DoctorGetDTO(entity.getDoctor());
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

    public DoctorGetDTO getDoctor() {
        return doctor;
    }

    public PatientGetDTO getPatient() {
        return patient;
    }
}
