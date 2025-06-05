package com.ubs.ubs.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ubs.ubs.entities.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class ScheduleRequestDTO {

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

    @NotNull(message = "Day of week cannot be null")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // ou HH:mm:ss
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // ou HH:mm:ss
    private LocalTime endTime;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}