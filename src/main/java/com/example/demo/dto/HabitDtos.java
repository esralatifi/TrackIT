package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class HabitDtos {

    public record HabitCreateRequest(
            @NotBlank(message = "name is required") String name,
            String description,
            @NotNull(message = "goalPerWeek is required") @Min(value = 1, message = "goalPerWeek must be at least 1") Integer goalPerWeek,
            @NotNull(message = "categoryId is required") Long categoryId
    ) {}

    public record HabitUpdateRequest(
            @NotBlank(message = "name is required") String name,
            String description,
            @NotNull(message = "goalPerWeek is required") @Min(value = 1, message = "goalPerWeek must be at least 1") Integer goalPerWeek,
            @NotNull(message = "categoryId is required") Long categoryId,
            @NotNull(message = "active is required") Boolean active
    ) {}

    public record HabitResponse(
            Long id,
            String name,
            String description,
            Integer goalPerWeek,
            boolean active,
            Instant createdAt,
            Long userId,
            Long categoryId
    ) {}
}
