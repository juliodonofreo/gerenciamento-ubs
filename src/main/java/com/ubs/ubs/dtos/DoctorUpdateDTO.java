package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.enums.Specialization;

public class DoctorUpdateDTO extends UserUpdateDTO{

    private Specialization specialization;


    public DoctorUpdateDTO() {
    }

    public DoctorUpdateDTO(String name, String email, String password, Specialization specialization) {
        super(name, email, password);
        this.specialization = specialization;
    }

    public DoctorUpdateDTO(Doctor entity) {
        super(entity.getName(), entity.getEmail(), entity.getPassword());
        this.specialization = entity.getSpecialization();
    }

    public Specialization getSpecialization() {
        return specialization;
    }
}
