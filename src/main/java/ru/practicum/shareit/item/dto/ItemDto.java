package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLink;
import ru.practicum.shareit.item.Comment.model.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLink lastBooking;
    private BookingLink nextBooking;
    private List<CommentDto> comments;
    private long requestId;
}
