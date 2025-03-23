package com.ubs.ubs.controllers;

import com.ubs.ubs.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/trigger-reminders")
    public ResponseEntity<String> triggerReminders() {
        appointmentService.checkAndSendAppointmentReminders();
        return ResponseEntity.ok("Lembretes acionados manualmente!");
    }
}