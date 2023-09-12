package ru.practicum.shareit.exceptions.model;

public class HandleDuplicatedEmailException extends RuntimeException {
    public HandleDuplicatedEmailException(String message) {
        super(message);
    }
}
