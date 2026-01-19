package com.example.demo.repository;

import java.util.Optional;
import com.example.demo.model.HabitLog;
import com.example.demo.model.HabitLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    Optional<com.example.demo.model.HabitLog> findTopByHabitIdOrderByDateDesc(Long habitId);

    long countByHabitIdAndDateBetweenAndStatus(Long habitId, java.time.LocalDate from, java.time.LocalDate to, com.example.demo.model.HabitLogStatus status);

    Optional<com.example.demo.model.HabitLog> findByHabitIdAndDate(Long habitId, java.time.LocalDate date);


    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitLog> findByHabitIdAndDateBetween(Long habitId, LocalDate from, LocalDate to);

    List<HabitLog> findByHabitIdAndDateBetweenAndStatus(Long habitId, LocalDate from, LocalDate to, HabitLogStatus status);
}
