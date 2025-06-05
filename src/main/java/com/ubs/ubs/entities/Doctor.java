package com.ubs.ubs.entities;

import com.ubs.ubs.entities.enums.Specialization;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    public Specialization specialization;

    private String crm;


    @OneToMany(mappedBy = "doctor")
    public List<Appointment> appointments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "health_unit_id")
    private HealthUnit healthUnit;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Schedule> schedules = new HashSet<>();


    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setDoctor(this);
    }

    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setDoctor(null);
    }

    public Doctor() {
    }

    public Doctor(Long id, String name, String email, String password, Specialization specialization, HealthUnit healthUnit, String crm) {
        super(id, name, email, password);
        this.specialization = specialization;
        this.healthUnit = healthUnit;
        this.crm = crm;
    }


    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public HealthUnit getHealthUnit() {
        return healthUnit;
    }

    public void setHealthUnit(HealthUnit healthUnit) {
        this.healthUnit = healthUnit;
    }
}
