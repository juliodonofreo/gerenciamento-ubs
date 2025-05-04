package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.enums.AppointmentState;

import java.time.Instant;

public class AppointmentUpdateDTO {
    private Instant date;
    private String diagnosis;
    private AppointmentState state;
    private String type;
    private Long doctorId;
    private Long patientId;

    public String getDiagnosis() {
        return diagnosis;
    }

    public Instant getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public AppointmentState getState() {
        return state;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }
}