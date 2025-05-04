package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.enums.Specialization;

public class DoctorUpdateDTO extends UserUpdateDTO{

    private Specialization specialization;

    private String crm;

    private Long healthUnitId;


    public DoctorUpdateDTO() {
    }

    public DoctorUpdateDTO(String name, String email, String password, Specialization specialization, Long healthUnitId, String crm) {
        super(name, email, password);
        this.specialization = specialization;
        this.healthUnitId = healthUnitId;
        this.crm = crm;
    }

    public DoctorUpdateDTO(Doctor entity) {
        super(entity.getName(), entity.getEmail(), entity.getPassword());
        this.specialization = entity.getSpecialization();
        this.healthUnitId = entity.getHealthUnit().getId();
        this.crm = entity.getCrm();
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Long getHealthUnitId() {
        return healthUnitId;
    }

    public String getCrm() {
        return crm;
    }
}
