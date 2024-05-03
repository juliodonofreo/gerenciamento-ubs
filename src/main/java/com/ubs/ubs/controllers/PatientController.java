package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.*;
import com.ubs.ubs.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping
    public ResponseEntity<Page<PatientGetDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }


    @GetMapping(value = "/{cpf}")
    public ResponseEntity<UserGetDTO> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok().body(service.findByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<PatientGetDTO> insert(@Valid @RequestBody PatientInsertDTO dto) {
        PatientGetDTO getDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{cpf}")
                .buildAndExpand(dto.getCpf())
                .toUri();
        return ResponseEntity.created(uri).body(getDto);
    }


    @PutMapping
    public ResponseEntity<PatientGetDTO> updatePatient(@Valid @RequestBody PatientInsertDTO dto) {
        PatientGetDTO getDto = service.update(dto);
        return ResponseEntity.ok().body(getDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDoctor() {
        service.delete();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dependents")
    public ResponseEntity<List<DependentGetDTO>> findAllDependents(){
        return ResponseEntity.ok().body(service.findAllDependents());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dependents/{id}")
    public ResponseEntity<DependentGetDTO> findDependentsById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findDependentsById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/dependents")
    public ResponseEntity<DependentGetDTO> addDependent(@Valid @RequestBody DependentInsertDTO dto){
        DependentGetDTO getDTO = service.addDependent(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(getDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(getDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/dependents/{id}")
    public ResponseEntity<DependentGetDTO> updateDependent(@PathVariable Long id, @Valid @RequestBody DependentInsertDTO dto){
        DependentGetDTO getDTO = service.updateDependent(id, dto);
        return ResponseEntity.ok().body(getDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/dependents/{id}")
    public ResponseEntity<Void> deleteDependent(@PathVariable Long id){
        service.deleteDependent(id);
        return ResponseEntity.noContent().build();
    }
}
