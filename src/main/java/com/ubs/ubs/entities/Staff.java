package com.ubs.ubs.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("STAFF")
public class Staff extends User {


    private String type;

    @ManyToOne
    @JoinColumn(name = "health_unit_id")
    private HealthUnit healthUnit;

    public Staff() {
    }
    public Staff(Long id, String name, String email, String password, HealthUnit healthUnit, String type) {
        super(id, name, email, password);
        this.healthUnit = healthUnit;
        this.type = type;
    }


    public Staff(Long id, String name, String email, String password, String type) {
        super(id, name, email, password);
        this.type = type;
    }

    public HealthUnit getHealthUnit() { return healthUnit; }
    public void setHealthUnit(HealthUnit healthUnit) { this.healthUnit = healthUnit; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}