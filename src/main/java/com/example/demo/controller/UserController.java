package com.example.demo.controller;

import com.example.demo.dto.UserDtos;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtos.UserResponse create(@Valid @RequestBody UserDtos.UserCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<UserDtos.UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDtos.UserResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserDtos.UserResponse updateEmail(@PathVariable Long id,
                                             @Valid @RequestBody UserDtos.UserUpdateRequest req) {
        return service.updateEmail(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
