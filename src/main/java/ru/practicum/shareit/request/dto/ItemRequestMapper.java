package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items == null ? Collections.emptyList() : items)
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestdto) {
        return ItemRequest.builder()
                .id(itemRequestdto.getId())
                .description(itemRequestdto.getDescription())
                .created(itemRequestdto.getCreated())
                .build();
    }
}