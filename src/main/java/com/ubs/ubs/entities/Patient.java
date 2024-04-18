package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("1")
public class Patient extends User {

    @Column(unique = true)
    private String cpf;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant birth_date;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();

    public Patient(){
        super();
    }

    public Patient(Long id, String name, String email, String password, String cpf, Instant birth_date) {
        super(id, name, email, password);
        this.cpf = cpf;
        this.birth_date = birth_date;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
