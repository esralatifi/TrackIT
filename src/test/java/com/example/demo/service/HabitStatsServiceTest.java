package com.example.demo.service;

import com.example.demo.dto.HabitStatsDtos;
import com.example.demo.model.Habit;
import com.example.demo.model.HabitLog;
import com.example.demo.model.HabitLogStatus;
import com.example.demo.repository.HabitLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitStatsServiceTest {

    @Mock
    private HabitService habitService;

    @Mock
    private HabitLogRepository logRepo;

    private HabitStatsService statsService;

    @BeforeEach
    void setup() {
        statsService = new HabitStatsService(habitService, logRepo);
    }

    @Test
    void getStats_calculatesStreak_consecutiveCompletedDays() {
        long habitId = 1L;

        Habit habit = new Habit();
        habit.setGoalPerWeek(7);
        when(habitService.findEntity(habitId)).thenReturn(habit);

        LocalDate latest = LocalDate.of(2026, 1, 19);

        // latest log exists
        HabitLog latestLog = new HabitLog(latest, HabitLogStatus.COMPLETED, null);
        when(logRepo.findTopByHabitIdOrderByDateDesc(habitId)).thenReturn(Optional.of(latestLog));

        // completed on 19,18,17; not completed on 16 => streak = 3
        when(logRepo.findByHabitIdAndDate(habitId, LocalDate.of(2026, 1, 19)))
                .thenReturn(Optional.of(new HabitLog(LocalDate.of(2026, 1, 19), HabitLogStatus.COMPLETED, null)));
        when(logRepo.findByHabitIdAndDate(habitId, LocalDate.of(2026, 1, 18)))
                .thenReturn(Optional.of(new HabitLog(LocalDate.of(2026, 1, 18), HabitLogStatus.COMPLETED, null)));
        when(logRepo.findByHabitIdAndDate(habitId, LocalDate.of(2026, 1, 17)))
                .thenReturn(Optional.of(new HabitLog(LocalDate.of(2026, 1, 17), HabitLogStatus.COMPLETED, null)));
        when(logRepo.findByHabitIdAndDate(habitId, LocalDate.of(2026, 1, 16)))
                .thenReturn(Optional.empty());

        // weekly completion count (value not essential for this test, but required by method)
        when(logRepo.countByHabitIdAndDateBetweenAndStatus(
                eq(habitId),
                any(LocalDate.class),
                any(LocalDate.class),
                eq(HabitLogStatus.COMPLETED)
        )).thenReturn(1L);

        HabitStatsDtos.HabitStatsResponse res = statsService.getStats(habitId, LocalDate.of(2026, 1, 19));

        assertEquals(habitId, res.habitId());
        assertEquals(3, res.currentStreak());
        assertEquals(LocalDate.of(2026, 1, 19), res.streakEndingOn());
    }

    @Test
    void getStats_calculatesWeeklyCompletionPercent_roundedTo2Decimals() {
        long habitId = 1L;

        Habit habit = new Habit();
        habit.setGoalPerWeek(7);
        when(habitService.findEntity(habitId)).thenReturn(habit);

        // streak irrelevant here: no logs => streak 0
        when(logRepo.findTopByHabitIdOrderByDateDesc(habitId)).thenReturn(Optional.empty());

        // completed 3 times in week => 42.857... => 42.86
        when(logRepo.countByHabitIdAndDateBetweenAndStatus(
                eq(habitId),
                eq(LocalDate.of(2026, 1, 19)),
                eq(LocalDate.of(2026, 1, 25)),
                eq(HabitLogStatus.COMPLETED)
        )).thenReturn(3L);

        HabitStatsDtos.HabitStatsResponse res = statsService.getStats(habitId, LocalDate.of(2026, 1, 19));

        assertEquals(3L, res.completedThisWeek());
        assertEquals(7, res.goalPerWeek());
        assertEquals(42.86, res.completionPercent(), 0.0001);
        assertEquals(LocalDate.of(2026, 1, 19), res.weekStart());
        assertEquals(LocalDate.of(2026, 1, 25), res.weekEnd());
        assertEquals(0, res.currentStreak());
        assertNull(res.streakEndingOn());
    }
}
