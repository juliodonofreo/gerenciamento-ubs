package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Appointment;

import java.time.Instant;

public class AppointmentGetDTO {

    private Long id;
    private Instant date;
    private String diagnosis;

    private DoctorGetMinDTO doctor;
    private PatientGetMinDTO patient;

    public AppointmentGetDTO(Long id, Instant date, String diagnosis) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
    }

    public AppointmentGetDTO(Appointment entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();

        this.patient = new PatientGetMinDTO(entity.getPatient());
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

    public PatientGetMinDTO getPatient() {
        return patient;
    }
}
