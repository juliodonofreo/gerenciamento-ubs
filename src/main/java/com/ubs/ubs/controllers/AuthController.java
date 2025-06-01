package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.EmailDTO;
import com.ubs.ubs.dtos.NewPasswordDTO;
import com.ubs.ubs.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@RequestBody @Valid EmailDTO body) {
        service.createRecoverToken(body);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/new-password")
    public ResponseEntity<Void> saveNewPassword(@RequestBody @Valid NewPasswordDTO body) {
        service.saveNewPassword(body);
        return ResponseEntity.noContent().build();
    }
}