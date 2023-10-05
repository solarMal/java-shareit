package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(toItemDto(booking.getItem()))
                .booker(toUserDto(booking.getBooker()))
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus() == null ? Status.WAITING : bookingDto.getStatus())
                .item(item)
                .booker(booker)
                .build();
    }
}