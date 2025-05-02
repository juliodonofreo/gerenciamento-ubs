package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.enums.Specialization;

public class DoctorUpdateDTO extends UserUpdateDTO{

    private Specialization specialization;

    private Long healthUnitId;


    public DoctorUpdateDTO() {
    }

    public DoctorUpdateDTO(String name, String email, String password, Specialization specialization, Long healthUnitId) {
        super(name, email, password);
        this.specialization = specialization;
        this.healthUnitId = healthUnitId;
    }

    public DoctorUpdateDTO(Doctor entity) {
        super(entity.getName(), entity.getEmail(), entity.getPassword());
        this.specialization = entity.getSpecialization();
        this.healthUnitId = entity.getHealthUnit().getId();
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Long getHealthUnitId() {
        return healthUnitId;
    }
}
