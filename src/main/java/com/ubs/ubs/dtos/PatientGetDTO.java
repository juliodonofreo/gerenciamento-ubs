package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PatientGetDTO extends UserGetDTO{
    private String cpf;
    private Instant birth_date;

    private List<AppointmentPatientGetDTO> appointments = new ArrayList<>();

    public PatientGetDTO() {
    }

    public PatientGetDTO(String name, String email, String cpf, Instant birth_date) {
        super(name, email);
        this.cpf = cpf;
        this.birth_date = birth_date;

    }

    public PatientGetDTO(Patient entity) {
        super(entity.getName(), entity.getEmail());
        this.cpf = entity.getCpf();
        this.birth_date = entity.getBirth_date();

        entity.getAppointments().forEach(x ->{
            appointments.add(new AppointmentPatientGetDTO(x));
        });
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public List<AppointmentPatientGetDTO> getAppointments() {
        return appointments;
    }

    @Override
    public String toString() {
        return "PatientGetDTO{" +
                "cpf='" + cpf + '\'' +
                ", birth_date=" + birth_date +
                ", appointments=" + appointments +
                '}';
    }
}
