package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.enums.Specialization;

import java.util.ArrayList;
import java.util.List;

public class DoctorGetDTO extends UserGetDTO {

    private Specialization specialization;

    public List<AppointmentPatientGetDTO> appointments = new ArrayList<>();

    public DoctorGetDTO() {
    }

    public DoctorGetDTO(Long id, String name, String email, Specialization specialization) {
        super(id, name, email);
        this.specialization = specialization;
    }

    public DoctorGetDTO(Doctor entity) {
        super(entity.getId(), entity.getName(), entity.getEmail());
        this.specialization = entity.getSpecialization();

        entity.getAppointments().forEach((x) -> {
                    appointments.add(new AppointmentPatientGetDTO(x));
                }
        );
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public List<AppointmentPatientGetDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentPatientGetDTO> appointments) {
        this.appointments = appointments;
    }
}
