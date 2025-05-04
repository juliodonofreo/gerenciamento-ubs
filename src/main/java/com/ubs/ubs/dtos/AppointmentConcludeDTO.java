package com.ubs.ubs.dtos;

import java.time.LocalDate;

public record AppointmentConcludeDTO(
        String observations,
        String examType,
        LocalDate examDate
) {}