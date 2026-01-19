package com.example.demo.service;

import com.example.demo.dto.ReminderDtos;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Habit;
import com.example.demo.model.Reminder;
import com.example.demo.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReminderService {

    private final ReminderRepository repo;
    private final HabitService habitService;

    public ReminderService(ReminderRepository repo, HabitService habitService) {
        this.repo = repo;
        this.habitService = habitService;
    }

    public ReminderDtos.ReminderResponse upsert(Long habitId, ReminderDtos.ReminderUpsertRequest req) {
        Habit habit = habitService.findEntity(habitId);

        Reminder reminder = repo.findByHabitId(habitId).orElseGet(Reminder::new);
        reminder.setHabit(habit);
        reminder.setReminderTime(req.reminderTime());
        reminder.setEnabled(req.enabled());

        Reminder saved = repo.save(reminder);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ReminderDtos.ReminderResponse get(Long habitId) {
        Reminder r = repo.findByHabitId(habitId)
                .orElseThrow(() -> new NotFoundException("Reminder not found for habitId: " + habitId));
        return toResponse(r);
    }

    public void delete(Long habitId) {
        repo.findByHabitId(habitId).ifPresent(repo::delete);
    }

    private ReminderDtos.ReminderResponse toResponse(Reminder r) {
        return new ReminderDtos.ReminderResponse(
                r.getId(),
                r.getReminderTime(),
                r.isEnabled(),
                r.getHabit().getId()
        );
    }
}
