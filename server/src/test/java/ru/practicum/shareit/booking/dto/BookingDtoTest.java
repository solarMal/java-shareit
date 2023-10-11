package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {
    private static final LocalDateTime START = LocalDateTime.now().minusHours(1);
    private static final LocalDateTime END = LocalDateTime.now().plusHours(1);

    @Autowired
    private JacksonTester json;

    @Test
    void testBookingDto() throws IOException {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(START)
                .end(END)
                .itemId(1)
                .item(ItemDto.builder()
                        .id(1)
                        .name("item")
                        .description("item description")
                        .available(true)
                        .build())
                .status(Status.APPROVED)
                .booker(UserDto.builder().id(2).build())
                .state(State.CURRENT)
                .build();
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(START.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(END.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(Status.APPROVED.toString());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.state")
                .isEqualTo(State.CURRENT.toString());
    }
}
