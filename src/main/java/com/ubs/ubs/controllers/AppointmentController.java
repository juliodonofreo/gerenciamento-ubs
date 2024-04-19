package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentGetDTO>> findAll(){
        return ResponseEntity.ok().body(appointmentService.findAll());
    }
}
