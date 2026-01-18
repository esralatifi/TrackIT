package com.example.demo.exception;

import java.time.Instant;
import java.util.List;

public class ApiError {
    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String message;
    private final List<FieldViolation> violations;

    public ApiError(int status, String error, String message, List<FieldViolation> violations) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.violations = violations;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<FieldViolation> getViolations() { return violations; }

    public record FieldViolation(String field, String message) {}
}
