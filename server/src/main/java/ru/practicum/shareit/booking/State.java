package ru.practicum.shareit.booking;

import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isBlank(state)) {
            return State.ALL;
        }
        if (Stream.of(State.values()).anyMatch(s -> s.name().equals(state))) {
            return State.valueOf(state);
        } else {
            throw new IncorrectStateException("Unknown state: " + state);
        }
    }
}