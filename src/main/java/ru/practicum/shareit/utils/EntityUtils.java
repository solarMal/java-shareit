package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.IncorrectStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class EntityUtils {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private static final Map<State, Predicate<Booking>> STATE_FILTER = new EnumMap<>(State.class);

    static {
        STATE_FILTER.put(State.ALL, b -> true);
        STATE_FILTER.put(State.PAST, b -> b.getEnd().isBefore(LocalDateTime.now()));
        STATE_FILTER.put(State.FUTURE, b -> b.getStart().isAfter(LocalDateTime.now()));
        STATE_FILTER.put(State.CURRENT, b -> b.getStart().isBefore(LocalDateTime.now())
                && b.getEnd().isAfter(LocalDateTime.now()));
        STATE_FILTER.put(State.WAITING, b -> b.getStatus().equals(Status.WAITING));
        STATE_FILTER.put(State.REJECTED, b -> b.getStatus().equals(Status.REJECTED));
    }

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

    public static Predicate<Booking> stateBy(State state) {
        return STATE_FILTER.getOrDefault(state, b -> {
            throw new IncorrectStateException("Unknown state: " + state);
        });
    }
}