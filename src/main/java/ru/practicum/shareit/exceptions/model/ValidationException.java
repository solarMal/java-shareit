package ru.practicum.shareit.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus()
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
