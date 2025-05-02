package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PATIENT")
public class Patient extends User {

    @Column(unique = true)
    private String cpf;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant birth_date;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "companion")
    private List<Dependent> dependents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "health_unit_id")
    private HealthUnit healthUnit;


    public Patient(){
        super();
    }

    public Patient(Long id, String name, String email, String password, String cpf, Instant birth_date, HealthUnit healthUnit) {
        super(id, name, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
        this.healthUnit = healthUnit;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Instant data_nascimento) {
        this.birth_date = data_nascimento;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Dependent> getDependents() {
        return dependents;
    }


    public HealthUnit getHealthUnit() {
        return healthUnit;
    }

    public void setHealthUnit(HealthUnit healthUnit) {
        this.healthUnit = healthUnit;
    }
}
