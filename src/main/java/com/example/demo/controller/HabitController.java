package com.example.demo.controller;

import com.example.demo.dto.HabitDtos;
import com.example.demo.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HabitController {

    private final HabitService service;

    public HabitController(HabitService service) {
        this.service = service;
    }

    @PostMapping("/api/users/{userId}/habits")
    @ResponseStatus(HttpStatus.CREATED)
    public HabitDtos.HabitResponse create(@PathVariable Long userId,
                                          @Valid @RequestBody HabitDtos.HabitCreateRequest req) {
        return service.create(userId, req);
    }

    @GetMapping("/api/users/{userId}/habits")
    public List<HabitDtos.HabitResponse> list(@PathVariable Long userId,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(required = false) Long categoryId,
                                              @RequestParam(required = false) Boolean active,
                                              @RequestParam(required = false) String sort) {
        return service.listForUser(userId, search, categoryId, active, sort);
    }

    @GetMapping("/api/habits/{habitId}")
    public HabitDtos.HabitResponse getById(@PathVariable Long habitId) {
        return service.getById(habitId);
    }

    @PutMapping("/api/habits/{habitId}")
    public HabitDtos.HabitResponse update(@PathVariable Long habitId,
                                          @Valid @RequestBody HabitDtos.HabitUpdateRequest req) {
        return service.update(habitId, req);
    }

    @DeleteMapping("/api/habits/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long habitId) {
        service.delete(habitId);
    }
}
