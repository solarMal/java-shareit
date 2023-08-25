package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemRepository {

    ItemDto save(ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto);

    ItemDto getItemById(Long id);

    List<ItemDto> getAllItems();
}
