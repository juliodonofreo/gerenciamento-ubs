package com.ubs.ubs.entities.enums;

import java.time.temporal.ChronoUnit;

public enum ReminderTimeUnit {
    HOURS, MINUTES;

    public ChronoUnit toChronoUnit() {
        return switch (this) {
            case HOURS -> ChronoUnit.HOURS;
            case MINUTES -> ChronoUnit.MINUTES;
            default -> throw new IllegalArgumentException("Unknown time unit: " + this);
        };
    }
}