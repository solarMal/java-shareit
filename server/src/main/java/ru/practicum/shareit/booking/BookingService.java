package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> findAllByBookerAndState(Integer bookerId, State state, Integer from, Integer size);

    List<BookingDto> findAllByOwnerAndState(Integer ownerId, State state, Integer from, Integer size);

    BookingDto findById(Integer userId, Integer bookingId);

    BookingDto create(Integer userId, BookingDto bookingDto);

    BookingDto updateStatus(Integer userId, Integer bookingId, Boolean approved);
}