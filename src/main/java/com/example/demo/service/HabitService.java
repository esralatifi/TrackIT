package com.example.demo.service;

import com.example.demo.dto.HabitDtos;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Habit;
import com.example.demo.model.User;
import com.example.demo.repository.HabitRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HabitService {

    private final HabitRepository habitRepo;
    private final UserService userService;
    private final CategoryService categoryService;

    public HabitService(HabitRepository habitRepo, UserService userService, CategoryService categoryService) {
        this.habitRepo = habitRepo;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public HabitDtos.HabitResponse create(Long userId, HabitDtos.HabitCreateRequest req) {
        User user = userService.findEntity(userId);
        Category category = categoryService.findEntity(req.categoryId());

        Habit h = new Habit();
        h.setName(req.name().trim());
        h.setDescription(req.description() == null ? null : req.description().trim());
        h.setGoalPerWeek(req.goalPerWeek());
        h.setUser(user);
        h.setCategory(category);

        return toResponse(habitRepo.save(h));
    }

    @Transactional(readOnly = true)
    public List<HabitDtos.HabitResponse> listForUser(Long userId, String search, Long categoryId, Boolean active, String sort) {
        // sort format: "createdAt,desc" or "createdAt,asc"
        Sort s = parseSort(sort);
        List<Habit> habits = habitRepo.findFiltered(
                userId,
                categoryId,
                active,
                (search == null || search.isBlank()) ? null : search.trim(),
                s
        );
        return habits.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public HabitDtos.HabitResponse getById(Long habitId) {
        return toResponse(findEntity(habitId));
    }

    public HabitDtos.HabitResponse update(Long habitId, HabitDtos.HabitUpdateRequest req) {
        Habit h = findEntity(habitId);
        Category category = categoryService.findEntity(req.categoryId());

        h.setName(req.name().trim());
        h.setDescription(req.description() == null ? null : req.description().trim());
        h.setGoalPerWeek(req.goalPerWeek());
        h.setActive(req.active());
        h.setCategory(category);

        return toResponse(h);
    }

    public void delete(Long habitId) {
        Habit h = findEntity(habitId);
        habitRepo.delete(h);
    }

    @Transactional(readOnly = true)
    public Habit findEntity(Long habitId) {
        return habitRepo.findById(habitId)
                .orElseThrow(() -> new NotFoundException("Habit not found: " + habitId));
    }

    private HabitDtos.HabitResponse toResponse(Habit h) {
        return new HabitDtos.HabitResponse(
                h.getId(),
                h.getName(),
                h.getDescription(),
                h.getGoalPerWeek(),
                h.isActive(),
                h.getCreatedAt(),
                h.getUser().getId(),
                h.getCategory().getId()
        );
    }

    private Sort parseSort(String sort) {
        // default
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        String dir = parts.length > 1 ? parts[1].trim().toLowerCase() : "desc";

        if (!field.equals("createdAt") && !field.equals("name")) {
            field = "createdAt";
        }
        Sort.Direction direction = dir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}
