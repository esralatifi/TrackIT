package com.example.demo.controller;

import com.example.demo.dto.ReminderDtos;
import com.example.demo.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReminderController {

    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @PutMapping("/api/habits/{habitId}/reminder")
    public ReminderDtos.ReminderResponse upsert(@PathVariable Long habitId,
                                                @Valid @RequestBody ReminderDtos.ReminderUpsertRequest req) {
        return service.upsert(habitId, req);
    }

    @GetMapping("/api/habits/{habitId}/reminder")
    public ReminderDtos.ReminderResponse get(@PathVariable Long habitId) {
        return service.get(habitId);
    }

    @DeleteMapping("/api/habits/{habitId}/reminder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long habitId) {
        service.delete(habitId);
    }
}
