package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private NearBooking lastBooking;
    private NearBooking nextBooking;
    private List<CommentDto> comments;

    @Data
    @Builder
    public static class NearBooking {
        private Integer id;
        private Integer bookerId;
    }
}