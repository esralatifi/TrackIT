package com.example.demo.service;

import com.example.demo.dto.HabitLogDtos;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Habit;
import com.example.demo.model.HabitLog;
import com.example.demo.repository.HabitLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HabitLogService {

    private final HabitLogRepository repo;
    private final HabitService habitService;

    public HabitLogService(HabitLogRepository repo, HabitService habitService) {
        this.repo = repo;
        this.habitService = habitService;
    }

    public HabitLogDtos.HabitLogResponse create(Long habitId, HabitLogDtos.HabitLogCreateRequest req) {
        Habit habit = habitService.findEntity(habitId);

        LocalDate date = req.date();
        if (repo.existsByHabitIdAndDate(habitId, date)) {
            throw new BadRequestException("Log already exists for habitId=" + habitId + " and date=" + date);
        }

        HabitLog log = new HabitLog(req.date(), req.status(), habit);
        return toResponse(repo.save(log));
    }

    @Transactional(readOnly = true)
    public List<HabitLogDtos.HabitLogResponse> list(Long habitId, LocalDate from, LocalDate to, String status) {
        // default range if not provided
        LocalDate f = (from != null) ? from : LocalDate.now().minusDays(30);
        LocalDate t = (to != null) ? to : LocalDate.now();

        if (f.isAfter(t)) {
            throw new BadRequestException("'from' must be <= 'to'");
        }

        List<HabitLog> logs;
        if (status == null || status.isBlank()) {
            logs = repo.findByHabitIdAndDateBetween(habitId, f, t);
        } else {
            logs = repo.findByHabitIdAndDateBetweenAndStatus(
                    habitId,
                    f,
                    t,
                    parseStatus(status)
            );
        }

        return logs.stream().map(this::toResponse).toList();
    }

    public void delete(Long logId) {
        HabitLog log = repo.findById(logId)
                .orElseThrow(() -> new NotFoundException("HabitLog not found: " + logId));
        repo.delete(log);
    }

    private HabitLogDtos.HabitLogResponse toResponse(HabitLog log) {
        return new HabitLogDtos.HabitLogResponse(
                log.getId(),
                log.getDate(),
                log.getStatus(),
                log.getHabit().getId()
        );
    }

    private com.example.demo.model.HabitLogStatus parseStatus(String status) {
        try {
            return com.example.demo.model.HabitLogStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status. Use COMPLETED, SKIPPED, or MISSED.");
        }
    }
}
