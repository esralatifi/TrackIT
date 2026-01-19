package com.example.demo.dto;

import com.example.demo.model.HabitLogStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class HabitLogDtos {

    public record HabitLogCreateRequest(
            @NotNull(message = "date is required") LocalDate date,
            @NotNull(message = "status is required") HabitLogStatus status
    ) {}

    public record HabitLogResponse(
            Long id,
            LocalDate date,
            HabitLogStatus status,
            Long habitId
    ) {}
}
