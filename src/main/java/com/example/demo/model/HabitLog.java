package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "habit_logs",
        uniqueConstraints = @UniqueConstraint(name = "uk_habit_date", columnNames = {"habit_id", "date"})
)
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HabitLogStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    public HabitLog() {}

    public HabitLog(LocalDate date, HabitLogStatus status, Habit habit) {
        this.date = date;
        this.status = status;
        this.habit = habit;
    }

    public Long getId() { return id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public HabitLogStatus getStatus() { return status; }
    public void setStatus(HabitLogStatus status) { this.status = status; }

    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }
}
