package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDtos {

    public record CategoryCreateRequest(
            @NotBlank(message = "name is required") String name
    ) {}

    public record CategoryUpdateRequest(
            @NotBlank(message = "name is required") String name
    ) {}

    public record CategoryResponse(Long id, String name) {}
}
