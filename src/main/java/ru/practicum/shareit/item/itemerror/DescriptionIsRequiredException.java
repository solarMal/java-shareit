package ru.practicum.shareit.item.itemerror;

public class DescriptionIsRequiredException extends RuntimeException {
    public DescriptionIsRequiredException(String message) {
        super(message);
    }
}
