package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.enums.AppointmentState;

import java.time.Instant;

public class AppointmentGetDTO {

    private Long id;
    private Instant date;
    private String diagnosis;
    private AppointmentState state;
    private String type;

    private DoctorGetMinDTO doctor;
    private PatientGetMinDTO patient;

    public AppointmentGetDTO(Long id, Instant date, String diagnosis, AppointmentState state, String type) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.state = state;
        this.type = type;
    }

    public AppointmentGetDTO(Appointment entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();
        this.state = entity.getState();
        this.type = entity.getType();

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

    public AppointmentState getState() {
        return state;
    }


    public String getType(){
        return type;
    }

    public DoctorGetMinDTO getDoctor() {
        return doctor;
    }

    public PatientGetMinDTO getPatient() {
        return patient;
    }
}
