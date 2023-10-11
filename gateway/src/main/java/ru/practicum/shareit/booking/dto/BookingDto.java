package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private Integer itemId;
    @FutureOrPresent
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime start;
    @Future
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime end;
}
