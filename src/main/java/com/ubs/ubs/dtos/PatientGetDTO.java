package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PatientGetDTO extends UserGetDTO{
    private String cpf;
    private Instant birth_date;

    private AddressGetDTO address;
    private final List<AppointmentDoctorGetDTO> appointments = new ArrayList<>();
    private final List<DependentGetDTO> dependents = new ArrayList<>();


    public PatientGetDTO() {
    }

    public PatientGetDTO(Long id, String name, String email, String cpf, Instant birth_date) {
        super(id, name, email);
        this.cpf = cpf;
        this.birth_date = birth_date;

    }

    public PatientGetDTO(Patient entity) {
        super(entity);
        this.cpf = entity.getCpf();
        this.birth_date = entity.getBirth_date();
        if (entity.getAddress() != null){
            this.address = new AddressGetDTO(entity.getAddress());
        }

        entity.getAppointments().forEach(x ->{
            appointments.add(new AppointmentDoctorGetDTO(x));
        });

        entity.getDependents().forEach(x ->{
            dependents.add(new DependentGetDTO(x));
        });
    }

    public String getCpf() {
        return cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public AddressGetDTO getAddress() {
        return address;
    }

    public List<AppointmentDoctorGetDTO> getAppointments() {
        return appointments;
    }

    public List<DependentGetDTO> getDependents() {
        return dependents;
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
