package ru.practicum.shareit.exceptions.model;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus()
public class AlreadyUsedException extends RuntimeException {
    public AlreadyUsedException(String message) {
        super(message);
    }
}