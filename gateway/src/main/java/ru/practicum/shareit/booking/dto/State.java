package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.exception.IncorrectStateException;

import java.util.stream.Stream;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State parseState(String state) {
        if (state.isBlank()) {
            return State.ALL;
        }
        if (Stream.of(State.values()).anyMatch(s -> s.name().equals(state))) {
            return State.valueOf(state);
        } else {
            throw new IncorrectStateException("Unknown state: " + state);
        }
    }
}
