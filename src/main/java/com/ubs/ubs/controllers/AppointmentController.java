package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.AppointmentConcludeDTO;
import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.dtos.AppointmentInsertDTO;
import com.ubs.ubs.dtos.AppointmentUpdateDTO;
import com.ubs.ubs.entities.Appointment;
import com.ubs.ubs.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @PreAuthorize("hasAnyRole('ROLE_UNIT', 'ROLE_STAFF')")
    @GetMapping
    public ResponseEntity<List<AppointmentGetDTO>> findAllByHealthUnit(){
        return ResponseEntity.ok().body(service.findAllByHealthUnit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentGetDTO> findById(@PathVariable Long id){
        AppointmentGetDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/patient")
    public ResponseEntity<List<AppointmentGetDTO>> findAllByPatient() {
        return ResponseEntity.ok().body(service.findAllByPatient());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<AppointmentGetDTO> insert(@RequestBody @Valid AppointmentInsertDTO dto){
        AppointmentGetDTO getDTO = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(getDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(getDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentGetDTO> update(@PathVariable Long id, @RequestBody @Valid AppointmentUpdateDTO dto){
        AppointmentGetDTO getDto = service.update(id, dto);
        return ResponseEntity.ok().body(getDto);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentGetDTO>> findAllByDoctor() {
        return ResponseEntity.ok().body(service.findAllByDoctor());
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PatchMapping("/{id}/conclude")
    public ResponseEntity<AppointmentGetDTO> concludeAppointment(
            @PathVariable Long id,
            @RequestBody @Valid AppointmentConcludeDTO dto) {
        return ResponseEntity.ok().body(service.concludeAppointment(id, dto));
    }

    @PreAuthorize("hasAnyRole('ROLE_UNIT', 'ROLE_PATIENT')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentGetDTO> cancelAppointment(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(service.cancelAppointment(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_UNIT', 'ROLE_STAFF')")
    @GetMapping("/week-summary")
    public ResponseEntity<Map<String, Long>> getAppointmentsWeekSummary() {
        Map<String, Long> weekSummary = service.getAppointmentsCountForCurrentWeekByDay();
        return ResponseEntity.ok(weekSummary);
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_UNIT', 'ROLE_DOCTOR')") // Ajuste as roles conforme necessário
    @GetMapping("/today")
    public ResponseEntity<List<AppointmentGetDTO>> getTodaysAppointments() {
        List<AppointmentGetDTO> todaysAppointments = service.getTodaysAppointmentsForCurrentUserUnit();
        return ResponseEntity.ok(todaysAppointments);
    }
}
