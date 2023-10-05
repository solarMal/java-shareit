package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemService service;

    @GetMapping
    public List<ItemDto> findAllByUserId(@RequestHeader(USER_HEADER) Integer userId,
                                         @RequestParam(value = "from", required = false, defaultValue = "0")
                                         Integer from,
                                         @RequestParam(value = "size", required = false, defaultValue = "10")
                                         Integer size) {
        return service.findAllByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Integer itemId,
                            @RequestHeader(USER_HEADER) Integer userId) {
        return service.findById(itemId, userId);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader(USER_HEADER) Integer userId) {
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Integer itemId,
                          @RequestHeader(USER_HEADER) Integer userId) {
        return service.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(required = false, name = "text") String text,
                                    @RequestParam(value = "from", required = false, defaultValue = "0")
                                    Integer from,
                                    @RequestParam(value = "size", required = false, defaultValue = "10")
                                    Integer size) {
        return service.findByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_HEADER) Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody CommentDto commentDto) {
        return service.addComment(userId, itemId, commentDto);
    }
}