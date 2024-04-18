package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.enums.Specialization;

import java.util.ArrayList;
import java.util.List;

public class DoctorGetMinDTO extends UserGetDTO {

    private Specialization specialization;


    public DoctorGetMinDTO() {
    }

    public DoctorGetMinDTO(String name, String email, Specialization specialization) {
        super(name, email);
        this.specialization = specialization;
    }

    public DoctorGetMinDTO(Doctor entity) {
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
