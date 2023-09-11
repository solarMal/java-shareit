package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.item.Comment.model.CommentDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String description;
    private Boolean available;
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
    private List<CommentDto> comments;
}
