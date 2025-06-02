package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.PatientCreateDTO;
import com.ubs.ubs.dtos.PatientResponseDTO;
import com.ubs.ubs.dtos.PatientUpdateDTO;
import com.ubs.ubs.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_UNIT', 'ROLE_STAFF')")
    public PatientResponseDTO create(@Valid @RequestBody PatientCreateDTO dto) {
        return patientService.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_STAFF')")
    public List<PatientResponseDTO> findAll() {
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_STAFF', 'ROLE_PATIENT')")
    public PatientResponseDTO findById(@PathVariable Long id) {
        return patientService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_STAFF', 'ROLE_PATIENT')")
    public PatientResponseDTO update(@PathVariable Long id, @Valid @RequestBody PatientUpdateDTO dto) {
        return patientService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_STAFF')")
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }
}