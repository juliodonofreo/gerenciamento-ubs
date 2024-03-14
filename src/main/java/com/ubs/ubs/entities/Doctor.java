package com.ubs.ubs.entities;

import com.ubs.ubs.enums.Specialization;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Doctor extends User {

    public Specialization specialization;

    public Doctor() {
    }

    public Doctor(Long id, String name, String email, String password, Specialization specialization) {
        super(id, name, email, password);
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
