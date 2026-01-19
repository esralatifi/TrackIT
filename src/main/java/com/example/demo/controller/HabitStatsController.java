package com.example.demo.controller;

import com.example.demo.dto.HabitStatsDtos;
import com.example.demo.service.HabitStatsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class HabitStatsController {

    private final HabitStatsService service;

    public HabitStatsController(HabitStatsService service) {
        this.service = service;
    }

    @GetMapping("/api/habits/{habitId}/stats")
    public HabitStatsDtos.HabitStatsResponse stats(@PathVariable Long habitId,
                                                   @RequestParam(required = false) LocalDate weekStart) {
        return service.getStats(habitId, weekStart);
    }
}
