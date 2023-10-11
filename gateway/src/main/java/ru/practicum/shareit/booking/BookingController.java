package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final BookingClient client;

    @GetMapping
    public ResponseEntity<Object> findAllByBookerAndState(@RequestHeader(USER_HEADER) Integer userId,
                                                          @RequestParam(defaultValue = "ALL") String state,
                                                          @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        State enumState = State.parseState(state);
        return client.findAllByBookerAndState(userId, enumState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwnerAndState(@RequestHeader(USER_HEADER) Integer userId,
                                                         @RequestParam(defaultValue = "ALL") String state,
                                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        State enumState = State.parseState(state);
        return client.findAllByOwnerAndState(userId, enumState, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable Integer bookingId,
                                           @RequestHeader(USER_HEADER) Integer userId) {
        return client.findById(bookingId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDto bookingDto,
                                         @RequestHeader(USER_HEADER) Integer userId) {
        return client.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(USER_HEADER) Integer userId,
                                               @PathVariable Integer bookingId,
                                               @RequestParam Boolean approved) {
        return client.updateStatus(userId, bookingId, approved);
    }
}