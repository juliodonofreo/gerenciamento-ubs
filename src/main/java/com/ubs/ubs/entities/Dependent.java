package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tb_dependent")
public class Dependent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Instant birth_date;

    @ManyToOne
    @JoinColumn(name = "companion_id")
    private Patient companion;

    public Dependent() {
    }

    public Dependent(Long id, String name, Instant birth_date) {
        this.id = id;
        this.name = name;
        this.birth_date = birth_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Instant birth_date) {
        this.birth_date = birth_date;
    }

    public Patient getCompanion() {
        return companion;
    }

    public void setCompanion(Patient companion) {
        this.companion = companion;
    }
}
