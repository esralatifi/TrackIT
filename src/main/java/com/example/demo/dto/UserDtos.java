package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDtos {

    public record UserCreateRequest(
            @NotBlank(message = "username is required") String username,
            @NotBlank(message = "email is required") @Email(message = "email must be valid") String email
    ) {}

    public record UserUpdateRequest(
            @NotBlank(message = "email is required") @Email(message = "email must be valid") String email
    ) {}

    public record UserResponse(Long id, String username, String email) {}
}
