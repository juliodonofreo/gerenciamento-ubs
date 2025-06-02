package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.HealthUnitCreateDTO;
import com.ubs.ubs.dtos.HealthUnitResponseDTO;
import com.ubs.ubs.dtos.HealthUnitUpdateDTO;
import com.ubs.ubs.services.HealthUnitService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-units")
public class HealthUnitController {

    @Autowired
    private HealthUnitService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HealthUnitResponseDTO create(@Valid @RequestBody HealthUnitCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<HealthUnitResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public HealthUnitResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public HealthUnitResponseDTO update(@PathVariable Long id, @Valid @RequestBody HealthUnitUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}