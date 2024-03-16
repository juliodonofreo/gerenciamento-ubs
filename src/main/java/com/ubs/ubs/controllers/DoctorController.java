package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.DoctorGetDTO;
import com.ubs.ubs.dtos.DoctorInsertDTO;
import com.ubs.ubs.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping(value = "/doctors")
public class DoctorController {

    @Autowired
    private DoctorService service;

    @GetMapping
    public ResponseEntity<Page<DoctorGetDTO>> findAll(Pageable pageable) {
        Page<DoctorGetDTO> getDTO = service.findAll(pageable);
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<DoctorGetDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<DoctorGetDTO> insert(@Valid @RequestBody DoctorInsertDTO dto) {
        DoctorGetDTO getDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(getDto);
    }

    @PutMapping
    public ResponseEntity<DoctorGetDTO> updateDoctor(@Valid @RequestBody DoctorInsertDTO dto, Authentication authentication) {
        DoctorGetDTO getDto = service.update(dto, authentication);
        return ResponseEntity.ok().body(getDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDoctor(Authentication authentication) {
        service.delete(authentication);
        return ResponseEntity.noContent().build();
    }
}
