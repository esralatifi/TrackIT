package com.example.demo.repository;

import com.example.demo.model.HabitLog;
import com.example.demo.model.HabitLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitLog> findByHabitIdAndDateBetween(Long habitId, LocalDate from, LocalDate to);

    List<HabitLog> findByHabitIdAndDateBetweenAndStatus(Long habitId, LocalDate from, LocalDate to, HabitLogStatus status);
}
