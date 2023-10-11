package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

@Component
@RequiredArgsConstructor
public class EntityUtils {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    public User getUserIfExists(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID: '%s' не найден", id)));
    }

    public Item getItemIfExists(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с ID: '%s' не найдена", id)));
    }

    public Booking getBookingIfExists(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с ID: '%s' не найдено", id)));
    }

    public ItemRequest getItemRequestIfExists(Integer requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с ID: '%s' не найден", requestId)));
    }
}