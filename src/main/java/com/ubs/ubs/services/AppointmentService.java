package com.ubs.ubs.services;

import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<AppointmentGetDTO> findAll(){
        List<Appointment> entities = appointmentRepository.findAll();
        return entities.stream().map(AppointmentGetDTO::new).toList();
    }

}
