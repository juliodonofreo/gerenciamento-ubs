package com.ubs.ubs.controllers;

import com.ubs.ubs.dtos.ScheduleRequestDTO;
import com.ubs.ubs.dtos.ScheduleResponseDTO;
import com.ubs.ubs.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody ScheduleRequestDTO requestDTO) {
        ScheduleResponseDTO createdSchedule = scheduleService.createSchedule(requestDTO);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByDoctor(@PathVariable Long doctorId) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByDoctorId(doctorId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long scheduleId) {
        ScheduleResponseDTO schedule = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long scheduleId,
                                                              @Valid @RequestBody ScheduleRequestDTO requestDTO) {
        ScheduleResponseDTO updatedSchedule = scheduleService.updateSchedule(scheduleId, requestDTO);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}