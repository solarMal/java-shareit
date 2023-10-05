package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemClient client;

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(USER_HEADER) Integer userId) {
        return client.findAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Integer itemId,
                                           @RequestHeader(USER_HEADER) Integer userId) {
        return client.findById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto,
                                         @RequestHeader(USER_HEADER) Integer userId) {
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                         @PathVariable Integer itemId,
                                         @RequestHeader(USER_HEADER) Integer userId) {
        return client.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByText(@RequestParam @NotNull String text) {
        return client.findByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Integer itemId,
                                             @RequestHeader(USER_HEADER) Integer userId,
                                             @RequestBody @Valid CommentDto commentDto) {
        return client.addComment(itemId, userId, commentDto);
    }
}
