package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final BookingService service;

    @GetMapping
    public List<BookingDto> findAllByBookerAndState(@RequestHeader(USER_HEADER) Integer bookerId,
                                                    @RequestParam(required = false, defaultValue = "ALL") String state,
                                                    @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return service.findAllByBookerAndState(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllByOwnerAndState(@RequestHeader(USER_HEADER) Integer ownerId,
                                                   @RequestParam(required = false) String state,
                                                   @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return service.findAllByOwnerAndState(ownerId, state, from, size);
    }

    @GetMapping("{bookingId}")
    public BookingDto findById(@RequestHeader(USER_HEADER) Integer userId,
                               @PathVariable Integer bookingId) {
        return service.findById(userId, bookingId);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(USER_HEADER) Integer userId,
                             @Valid @RequestBody BookingDto bookingDto) {
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(USER_HEADER) Integer userId,
                                   @PathVariable Integer bookingId,
                                   @RequestParam Boolean approved) {
        return service.updateStatus(userId, bookingId, approved);
    }
}