package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LastBookingDto {
    private long id;
    private long bookerId;
    private LocalDateTime lastDate;
}
