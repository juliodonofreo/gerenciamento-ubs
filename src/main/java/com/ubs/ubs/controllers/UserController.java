package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.UserFullDTO;
import com.ubs.ubs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserFullDTO> currentUser(){
        UserFullDTO dto = userService.currentUserDTO();
        return ResponseEntity.ok().body(dto);
    }
}
