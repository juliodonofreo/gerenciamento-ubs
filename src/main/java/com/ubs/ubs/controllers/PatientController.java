package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.PatientInsertDTO;
import com.ubs.ubs.dtos.PatientGetDTO;
import com.ubs.ubs.dtos.UserGetDTO;
import com.ubs.ubs.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/patients")
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping
    public ResponseEntity<Page<PatientGetDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    //    @GetMapping(value = "/{id}")
    public ResponseEntity<UserGetDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<UserGetDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<PatientGetDTO> insert(@Valid @RequestBody PatientInsertDTO dto) {
        PatientGetDTO getDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(getDto);
    }

    @PutMapping
    public ResponseEntity<PatientGetDTO> updatePatient(@Valid @RequestBody PatientInsertDTO dto, Authentication authentication) {
        PatientGetDTO getDto = service.update(dto, authentication);
        return ResponseEntity.ok().body(getDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDoctor(Authentication authentication) {
        service.delete(authentication);
        return ResponseEntity.noContent().build();
    }
}
