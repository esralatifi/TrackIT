package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class ReminderDtos {

    public record ReminderUpsertRequest(
            @NotNull(message = "reminderTime is required") LocalTime reminderTime,
            @NotNull(message = "enabled is required") Boolean enabled
    ) {}

    public record ReminderResponse(
            Long id,
            LocalTime reminderTime,
            boolean enabled,
            Long habitId
    ) {}
}
