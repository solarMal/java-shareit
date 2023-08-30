package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(ItemDto item) {

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest() : null,
                item.getAvailable()
                );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getOwner(),
                itemDto.getRequest() != null ? itemDto.getRequest() : null,
                itemDto.getAvailable()
                );
    }
}
