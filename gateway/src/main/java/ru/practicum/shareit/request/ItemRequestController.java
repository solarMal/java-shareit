package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient client;

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader(USER_HEADER) Integer userId) {
        return client.findAllByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader(USER_HEADER) Integer userId,
                                           @PathVariable Integer id) {
        return client.findById(userId, id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllByParams(@RequestHeader(USER_HEADER) Integer userId,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        return client.findAllByParams(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) Integer userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return client.create(userId, itemRequestDto);
    }
}