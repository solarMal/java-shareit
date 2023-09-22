package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @GetMapping
    public List<ItemRequestDto> findAllByUserId(@RequestHeader(USER_HEADER) Integer userId) {
        return service.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader(USER_HEADER) Integer userId,
                                   @PathVariable("requestId") Integer requestId) {
        return service.findById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllByParams(@RequestHeader(USER_HEADER) Integer userId,
                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.findAllByParams(userId, PageRequest.of(from, size, Sort.by("created").descending()));
    }

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(USER_HEADER) Integer userId) {
        return service.create(itemRequestDto, userId);
    }
}