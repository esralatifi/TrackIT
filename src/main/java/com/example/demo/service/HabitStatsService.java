package com.example.demo.service;

import com.example.demo.dto.HabitStatsDtos;
import com.example.demo.model.Habit;
import com.example.demo.model.HabitLog;
import com.example.demo.model.HabitLogStatus;
import com.example.demo.repository.HabitLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class HabitStatsService {

    private final HabitService habitService;
    private final HabitLogRepository logRepo;

    public HabitStatsService(HabitService habitService, HabitLogRepository logRepo) {
        this.habitService = habitService;
        this.logRepo = logRepo;
    }

    public HabitStatsDtos.HabitStatsResponse getStats(Long habitId, LocalDate weekStart) {
        Habit habit = habitService.findEntity(habitId);

        LocalDate ws = (weekStart != null) ? weekStart : startOfWeek(LocalDate.now());
        LocalDate we = ws.plusDays(6);

        long completed = logRepo.countByHabitIdAndDateBetweenAndStatus(habitId, ws, we, HabitLogStatus.COMPLETED);
        int goal = habit.getGoalPerWeek();
        double percent = Math.min(100.0, (completed * 100.0) / goal);

        StreakResult streak = calculateStreak(habitId);

        return new HabitStatsDtos.HabitStatsResponse(
                habitId,
                streak.streak(),
                streak.endsOn(),
                ws,
                we,
                completed,
                goal,
                round2(percent)
        );
    }

    private StreakResult calculateStreak(Long habitId) {
        Optional<HabitLog> latestOpt = logRepo.findTopByHabitIdOrderByDateDesc(habitId);
        if (latestOpt.isEmpty()) {
            return new StreakResult(0, null);
        }

        LocalDate cursor = latestOpt.get().getDate();
        int streak = 0;

        while (true) {
            Optional<HabitLog> logOpt = logRepo.findByHabitIdAndDate(habitId, cursor);
            if (logOpt.isPresent() && logOpt.get().getStatus() == HabitLogStatus.COMPLETED) {
                streak++;
                cursor = cursor.minusDays(1);
            } else {
                break;
            }
        }

        return new StreakResult(streak, latestOpt.get().getDate());
    }

    private LocalDate startOfWeek(LocalDate d) {
        // Monday-based week (common in Europe)
        int diff = d.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return d.minusDays(diff);
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private record StreakResult(int streak, LocalDate endsOn) {}
}
