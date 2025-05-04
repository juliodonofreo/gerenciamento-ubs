package com.ubs.ubs.controllers;

import com.ubs.ubs.entities.enums.Specialization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpecializationController {

    @GetMapping("/specializations")
    public ResponseEntity<List<String>> getSpecializations() {
        return ResponseEntity.ok(
                Arrays.stream(Specialization.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()));
    }
}