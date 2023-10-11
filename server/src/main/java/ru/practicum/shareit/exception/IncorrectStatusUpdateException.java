package ru.practicum.shareit.exception;

public class IncorrectStatusUpdateException extends RuntimeException {
    public IncorrectStatusUpdateException(String message) {
        super(message);
    }
}