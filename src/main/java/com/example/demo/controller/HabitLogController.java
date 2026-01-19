package com.example.demo.controller;

import com.example.demo.dto.HabitLogDtos;
import com.example.demo.service.HabitLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HabitLogController {

    private final HabitLogService service;

    public HabitLogController(HabitLogService service) {
        this.service = service;
    }

    @PostMapping("/api/habits/{habitId}/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public HabitLogDtos.HabitLogResponse create(@PathVariable Long habitId,
                                                @Valid @RequestBody HabitLogDtos.HabitLogCreateRequest req) {
        return service.create(habitId, req);
    }

    @GetMapping("/api/habits/{habitId}/logs")
    public List<HabitLogDtos.HabitLogResponse> list(@PathVariable Long habitId,
                                                    @RequestParam(required = false) LocalDate from,
                                                    @RequestParam(required = false) LocalDate to,
                                                    @RequestParam(required = false) String status) {
        return service.list(habitId, from, to, status);
    }

    @DeleteMapping("/api/logs/{logId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long logId) {
        service.delete(logId);
    }
}
