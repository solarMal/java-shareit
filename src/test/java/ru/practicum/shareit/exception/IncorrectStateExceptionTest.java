package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IncorrectStateExceptionTest {

    @Test
    void testMessage() {
        String message = "Сообщение исключения";
        Exception e = assertThrows(AlreadyExistException.class, () -> {
            throw new AlreadyExistException(message);
        });
        assertEquals(message, e.getMessage());
    }
}