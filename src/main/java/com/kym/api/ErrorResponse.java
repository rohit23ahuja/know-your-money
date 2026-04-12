package com.kym.api;

import java.time.Instant;

public class ErrorResponse {
    private String message;
    private Instant timestamp = Instant.now();

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
