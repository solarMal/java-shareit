package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.IncorrectStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntityUtilsTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private EntityUtils entityUtils;

    @Test
    void getUserIfExists() {
        User user = User.builder().id(1).build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        assertEquals(User.builder().id(1).build(), entityUtils.getUserIfExists(1));
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void getUserIfNotExists() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception e = assertThrows(NotFoundException.class, () -> entityUtils.getUserIfExists(1));
        assertEquals("Пользователь с ID: '1' не найден", e.getMessage());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemIfExists() {
        Item item = Item.builder().id(1).build();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        assertEquals(Item.builder().id(1).build(), entityUtils.getItemIfExists(1));
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemIfNotExists() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception e = assertThrows(NotFoundException.class, () -> entityUtils.getItemIfExists(1));
        assertEquals("Вещь с ID: '1' не найдена", e.getMessage());
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    void getBookingIfExists() {
        Booking booking = Booking.builder().id(1).build();
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        assertEquals(Booking.builder().id(1).build(), entityUtils.getBookingIfExists(1));
        verify(bookingRepository, times(1)).findById(anyInt());
    }

    @Test
    void getBookingIfNotExists() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception e = assertThrows(NotFoundException.class, () -> entityUtils.getBookingIfExists(1));
        assertEquals("Бронирование с ID: '1' не найдено", e.getMessage());
        verify(bookingRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemRequestIfExists() {
        ItemRequest itemRequest = ItemRequest.builder().id(1).build();
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));
        assertEquals(ItemRequest.builder().id(1).build(), entityUtils.getItemRequestIfExists(1));
        verify(itemRequestRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemRequestIfNotExists() {
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception e = assertThrows(NotFoundException.class, () -> entityUtils.getItemRequestIfExists(1));
        assertEquals("Запрос с ID: '1' не найден", e.getMessage());
        verify(itemRequestRepository, times(1)).findById(anyInt());
    }

    @Test
    void correctStateBy() {
        assertEquals(State.ALL, State.parseState(null));
    }

    @Test
    void incorrectStateBy() {
        String state = "COMPLETE";
        Exception e = assertThrows(IncorrectStateException.class, () -> State.parseState(state));
        assertEquals("Unknown state: COMPLETE", e.getMessage());
    }
}