package com.ubs.ubs.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ubs.ubs.entities.enums.DayOfWeek;

import java.time.LocalTime;

public class ScheduleResponseDTO {
    private Long id;
    private Long doctorId;
    private String doctorName; // Para conveniência
    private DayOfWeek dayOfWeek;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    // Construtor (opcional, mas útil para mapeamento)
    public ScheduleResponseDTO(Long id, Long doctorId, String doctorName, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}