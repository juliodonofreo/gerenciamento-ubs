package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.enums.Specialization;

import java.util.ArrayList;
import java.util.List;

public class DoctorGetDTO extends UserGetDTO {

    private Specialization specialization;

    public List<AppointmentDoctorGetDTO> appointments = new ArrayList<>();

    public DoctorGetDTO() {
    }

    public DoctorGetDTO(String name, String email, Specialization specialization) {
        super(name, email);
        this.specialization = specialization;
    }

    public DoctorGetDTO(Doctor entity) {
        super(entity.getName(), entity.getEmail());
        this.specialization = entity.getSpecialization();

        entity.getAppointments().forEach((x) -> {
                    appointments.add(new AppointmentDoctorGetDTO(x));
                }
        );
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public List<AppointmentDoctorGetDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDoctorGetDTO> appointments) {
        this.appointments = appointments;
    }
}
