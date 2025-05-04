package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.enums.AppointmentState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


import java.time.Instant;

public class AppointmentInsertDTO {

    @NotNull
    private Instant date;

    @NotBlank
    private String diagnosis;

    @NotNull
    private AppointmentState state;

    @NotBlank
    private String type;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    public AppointmentInsertDTO(){

    }

    public AppointmentInsertDTO(Instant date, AppointmentState state, String diagnosis, String type, Long doctorId, Long patientId) {
        this.date = date;
        this.state = state;
        this.diagnosis = diagnosis;
        this.type = type;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public AppointmentInsertDTO(Appointment entity) {
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();
        this.state = entity.getState();
        this.type = entity.getType();

        this.patientId = entity.getPatient().getId();
        this.doctorId = entity.getDoctor().getId();
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

    public Long getDoctorId() {
        return doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getType() {
        return type;
    }
}
