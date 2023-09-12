package ru.practicum.shareit.exceptions.model;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus()
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
