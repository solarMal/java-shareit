package ru.practicum.shareit.item.itemerror;

public class ExistingItemException extends RuntimeException {
    public ExistingItemException(String message) {
        super(message);
    }
}
