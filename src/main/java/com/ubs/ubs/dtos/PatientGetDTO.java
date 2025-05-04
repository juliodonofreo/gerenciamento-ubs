package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Patient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PatientGetDTO extends UserGetDTO{
    private String cpf;
    private Instant birth_date;

    private String address;

    private String phone;
    private final List<AppointmentDoctorGetDTO> appointments = new ArrayList<>();
    private final List<DependentGetDTO> dependents = new ArrayList<>();


    public PatientGetDTO() {
    }

    public PatientGetDTO(Long id, String name, String email, String cpf, Instant birth_date, String address,String phone) {
        super(id, name, email);
        this.cpf = cpf;
        this.birth_date = birth_date;
        this.address = address;
        this.phone = phone;

    }

    public PatientGetDTO(Patient entity) {
        super(entity);
        this.cpf = entity.getCpf();
        this.birth_date = entity.getBirth_date();
        this.address = entity.getAddress();
        this.phone = entity.getPhone();

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

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
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
