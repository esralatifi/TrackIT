package com.example.demo.dto;

import java.time.LocalDate;

public class HabitStatsDtos {

    public record HabitStatsResponse(
            Long habitId,
            int currentStreak,
            LocalDate streakEndingOn,
            LocalDate weekStart,
            LocalDate weekEnd,
            long completedThisWeek,
            int goalPerWeek,
            double completionPercent
    ) {}
}
