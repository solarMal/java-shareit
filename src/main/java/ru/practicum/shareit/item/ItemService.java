package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findAllByUserId(Integer userId);

    ItemDto findById(Integer itemId, Integer userId);

    ItemDto create(ItemDto itemDto, Integer userId);

    ItemDto update(ItemDto itemDto, Integer itemId, Integer userId);

    List<ItemDto> findByText(String text);

    CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto);
}