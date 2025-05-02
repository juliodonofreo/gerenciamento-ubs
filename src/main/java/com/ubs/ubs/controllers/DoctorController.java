package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.DoctorCreateDTO;
import com.ubs.ubs.dtos.DoctorResponseDTO;
import com.ubs.ubs.dtos.DoctorUpdateDTO;
import com.ubs.ubs.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_UNIT')")
    public DoctorResponseDTO create(@Valid @RequestBody DoctorCreateDTO dto) {
        return doctorService.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public List<DoctorResponseDTO> findAll() {
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT', 'ROLE_DOCTOR')")
    public DoctorResponseDTO findById(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public DoctorResponseDTO update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDTO dto, Authentication authentication) {
        return doctorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public void delete(@PathVariable Long id) {
        doctorService.delete(id);
    }
}