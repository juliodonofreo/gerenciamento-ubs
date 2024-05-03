package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.AppointmentGetDTO;
import com.ubs.ubs.dtos.AppointmentInsertDTO;
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

@RestController
@RequestMapping(value = "/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentGetDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentGetDTO> findById(@PathVariable Long id){
        AppointmentGetDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
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
    public ResponseEntity<AppointmentGetDTO> update(@PathVariable Long id, @RequestBody @Valid AppointmentInsertDTO dto){
        AppointmentGetDTO getDto = service.update(id, dto);
        return ResponseEntity.ok().body(getDto);
    }
}
