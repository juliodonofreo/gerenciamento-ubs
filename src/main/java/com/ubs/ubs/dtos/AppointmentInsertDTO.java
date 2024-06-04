package com.ubs.ubs.dtos;

import com.ubs.ubs.dtos.utils.ValidationErrorMessages;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.entities.enums.AppointmentState;
import org.hibernate.validator.constraints.Length;


import java.time.Instant;

public class AppointmentInsertDTO {

    private Long id;
    private Instant date;

    @Length(min = 3, message = ValidationErrorMessages.INVALID_DIAGNOSIS_LENGTH)
    private String diagnosis;
    private AppointmentState state;
    private PatientInsertDTO patient;
    private DoctorInsertDTO doctor;

    public AppointmentInsertDTO(){

    }

    public AppointmentInsertDTO(Long id, Instant date, String diagnosis, AppointmentState state, DoctorInsertDTO doctor, PatientInsertDTO patient) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.state = state;
        this.doctor = doctor;
        this.patient = patient;
    }

    public AppointmentInsertDTO(Appointment entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.diagnosis = entity.getDiagnosis();
        this.state = entity.getState();
        this.patient = new PatientInsertDTO(entity.getPatient());
        this.doctor = new DoctorInsertDTO(entity.getDoctor());
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

    public PatientInsertDTO getPatient() {
        return patient;
    }

    public DoctorInsertDTO getDoctor() {
        return doctor;
    }
}
