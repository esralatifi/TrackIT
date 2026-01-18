package com.example.demo.service;

import com.example.demo.dto.UserDtos;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserDtos.UserResponse create(UserDtos.UserCreateRequest req) {
        String username = req.username().trim();
        String email = req.email().trim();

        repo.findByUsernameIgnoreCase(username).ifPresent(u -> {
            throw new BadRequestException("Username already exists");
        });
        repo.findByEmailIgnoreCase(email).ifPresent(u -> {
            throw new BadRequestException("Email already exists");
        });

        User saved = repo.save(new User(username, email));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<UserDtos.UserResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserDtos.UserResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public UserDtos.UserResponse updateEmail(Long id, UserDtos.UserUpdateRequest req) {
        User u = findEntity(id);
        String email = req.email().trim();

        repo.findByEmailIgnoreCase(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Email already exists");
            }
        });

        u.setEmail(email);
        return toResponse(u);
    }

    public void delete(Long id) {
        User u = findEntity(id);
        repo.delete(u);
    }

    @Transactional(readOnly = true)
    public User findEntity(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    private UserDtos.UserResponse toResponse(User u) {
        return new UserDtos.UserResponse(u.getId(), u.getUsername(), u.getEmail());
    }
}
