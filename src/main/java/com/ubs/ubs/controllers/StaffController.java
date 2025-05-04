package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.StaffCreateDTO;
import com.ubs.ubs.dtos.StaffResponseDTO;
import com.ubs.ubs.dtos.StaffUpdateDTO;
import com.ubs.ubs.services.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService service;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_UNIT')")
    public StaffResponseDTO create(@Valid @RequestBody StaffCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_UNIT')")
    public List<StaffResponseDTO> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_UNIT')")
    public StaffResponseDTO update(@PathVariable Long id, @Valid @RequestBody StaffUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_UNIT')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}