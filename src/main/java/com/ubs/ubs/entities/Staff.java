package com.ubs.ubs.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("STAFF")
public class Staff extends User {

    @ManyToOne
    @JoinColumn(name = "health_unit_id")
    private HealthUnit healthUnit;

    public Staff() {
    }
    public Staff(Long id, String name, String email, String password, HealthUnit healthUnit) {
        super(id, name, email, password);
        this.healthUnit = healthUnit;
    }


    public Staff(Long id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public HealthUnit getHealthUnit() { return healthUnit; }
    public void setHealthUnit(HealthUnit healthUnit) { this.healthUnit = healthUnit; }
}