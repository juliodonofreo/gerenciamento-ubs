package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.DoctorDTO;
import com.ubs.ubs.dtos.PatientDTO;
import com.ubs.ubs.dtos.UserDTO;
import com.ubs.ubs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

//    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping(value = "/{email}")
    public ResponseEntity<UserDTO> findById(@PathVariable String email) {
        return ResponseEntity.ok().body(service.findByEmail(email));
    }

    @PostMapping(value = "/patient")
    public ResponseEntity<PatientDTO> insert(@RequestBody PatientDTO dto) {
        dto = service.insertPatient(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping(value = "/doctor")
    public ResponseEntity<DoctorDTO> insert(@RequestBody DoctorDTO dto) {
        dto = service.insertDoctor(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto, Authentication authentication) {
        dto = service.update(dto, authentication);
        return ResponseEntity.ok().body(dto);
    }
}
