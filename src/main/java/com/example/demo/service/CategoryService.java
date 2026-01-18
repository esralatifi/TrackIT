package com.example.demo.service;

import com.example.demo.dto.CategoryDtos;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public CategoryDtos.CategoryResponse create(CategoryDtos.CategoryCreateRequest req) {
        String name = req.name().trim();

        repo.findByNameIgnoreCase(name).ifPresent(c -> {
            throw new BadRequestException("Category already exists");
        });

        Category saved = repo.save(new Category(name));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryDtos.CategoryResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDtos.CategoryResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public CategoryDtos.CategoryResponse update(Long id, CategoryDtos.CategoryUpdateRequest req) {
        Category c = findEntity(id);
        String name = req.name().trim();

        repo.findByNameIgnoreCase(name).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Category already exists");
            }
        });

        c.setName(name);
        return toResponse(c);
    }

    public void delete(Long id) {
        Category c = findEntity(id);
        repo.delete(c);
    }

    @Transactional(readOnly = true)
    public Category findEntity(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Category not found: " + id));
    }

    private CategoryDtos.CategoryResponse toResponse(Category c) {
        return new CategoryDtos.CategoryResponse(c.getId(), c.getName());
    }
}
