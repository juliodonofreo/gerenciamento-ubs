package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.enums.Specialization;

public class DoctorGetDTO extends UserGetDTO {

    private Specialization specialization;

    public DoctorGetDTO() {
    }

    public DoctorGetDTO(String name, String email, Specialization specialization) {
        super(name, email);
        this.specialization = specialization;
    }

    public DoctorGetDTO(Doctor entity) {
        super(entity.getName(), entity.getEmail());
        this.specialization = entity.getSpecialization();
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
