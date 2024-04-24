package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.enums.Specialization;

public class DoctorGetMinDTO extends UserGetDTO {
    private Specialization specialization;


    public DoctorGetMinDTO() {
    }

    public DoctorGetMinDTO(Long id, String name, String email, Specialization specialization) {
        super(id, name, email);
        this.specialization = specialization;
    }

    public DoctorGetMinDTO(Doctor entity) {
        super(entity.getId(), entity.getName(), entity.getEmail());
        this.specialization = entity.getSpecialization();

    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

}
