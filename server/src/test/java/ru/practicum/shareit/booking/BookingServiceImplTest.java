package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;
    @Mock
    private EntityUtils entityUtils;
    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void findAllByBookerAndState() {
        when(repository.findByBookerId(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(createBooking())));
        when(repository.findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(repository.findByBookerIdCurrenState(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(createBooking())));
        when(repository.findByBookerIdAndEndIsBefore(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(repository.findByBookerIdAndStartIsAfter(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        List<BookingDto> result = service.findAllByBookerAndState(1, State.ALL, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        result = service.findAllByBookerAndState(1, State.REJECTED, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByBookerAndState(1, State.WAITING, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByBookerAndState(1, State.CURRENT, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        result = service.findAllByBookerAndState(1, State.PAST, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByBookerAndState(1, State.FUTURE, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByOwnerAndState() {
        when(repository.findBookingByItemOwnerId(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(createBooking())));
        when(repository.findBookingByItemOwnerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(repository.findBookingByItemOwnerCurrentState(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(createBooking())));
        when(repository.findBookingByItemOwnerIdAndEndIsBefore(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(repository.findBookingByItemOwnerIdAndStartIsAfter(anyInt(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        List<BookingDto> result = service.findAllByOwnerAndState(1, State.ALL, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        result = service.findAllByOwnerAndState(1, State.REJECTED, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByOwnerAndState(1, State.WAITING, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByOwnerAndState(1, State.CURRENT, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        result = service.findAllByOwnerAndState(1, State.PAST, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        result = service.findAllByOwnerAndState(1, State.FUTURE, 1, 1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById() {
        Booking booking = Booking.builder()
                .booker(User.builder().id(1).build())
                .item(Item.builder().owner(User.builder().id(2).build()).build())
                .start(LocalDateTime.of(2020, 1, 1, 0, 0))
                .end(LocalDateTime.of(2020, 2, 1, 0, 0))
                .status(Status.APPROVED)
                .build();
        when(entityUtils.getBookingIfExists(anyInt())).thenReturn(booking);
        assertEquals(toBookingDto(booking), service.findById(1, 1));
        verify(entityUtils, times(1)).getBookingIfExists(anyInt());
    }

    @Test
    void findByIdNotAvailable() {
        Booking booking = Booking.builder()
                .booker(User.builder().id(1).build())
                .item(Item.builder().owner(User.builder().id(2).build()).build())
                .build();
        when(entityUtils.getBookingIfExists(anyInt())).thenReturn(booking);
        Exception e = assertThrows(NotFoundException.class, () -> service.findById(3, 1));
        assertEquals("Бронирование с ID: '1' не найдено", e.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyInt());
    }

    @Test
    void create() {
        Item item = Item.builder()
                .available(true)
                .owner(User.builder().id(1).build())
                .build();
        var user = User.builder().id(2).build();
        var booking = Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        when(repository.save(any(Booking.class))).thenReturn(booking);
        BookingDto bookingDto = toBookingDto(booking);
        bookingDto.setItemId(1);
        BookingDto resultBookingDto = service.create(2, bookingDto);
        booking.setId(1);
        resultBookingDto.setId(1);
        assertEquals(toBookingDto(booking), resultBookingDto);
        verify(repository, times(1)).save(any(Booking.class));
    }

    @Test
    void createByItemOwner() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        Item item = Item.builder()
                .available(true)
                .owner(User.builder().id(1).build())
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        var exception = assertThrows(NotFoundException.class,
                () -> service.create(1, bookingDto));
        assertEquals("Вещь не может быть забронирована владельцем", exception.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void createWithStartEqualsEnd() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.of(2023, 1, 1, 0, 0))
                .end(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(Item.builder().available(true).build());
        Exception e = assertThrows(IncorrectDateException.class,
                () -> service.create(1, bookingDto));
        assertEquals("Дата начала позже или равна окончанию бронирования", e.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void createWithStartAfterEnd() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(1))
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(Item.builder().available(true).build());
        Exception e = assertThrows(IncorrectDateException.class,
                () -> service.create(1, bookingDto));
        assertEquals("Дата начала позже или равна окончанию бронирования", e.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void createWithEndNull() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1)
                .end(null)
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(Item.builder().available(true).build());
        Exception e = assertThrows(IncorrectDateException.class,
                () -> service.create(1, bookingDto));
        assertEquals("Неверно задана дата бронирования", e.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void createWithStartNull() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1)
                .start(null)
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(Item.builder().available(true).build());
        Exception e = assertThrows(IncorrectDateException.class,
                () -> service.create(1, bookingDto));
        assertEquals("Неверно задана дата бронирования", e.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void createWithStatus() {
        Item item = Item.builder().available(true)
                .owner(User.builder().id(1).build())
                .build();
        User user = User.builder().id(2).build();
        Booking booking = Booking.builder()
                .item(item)
                .booker(user)
                .start(LocalDateTime.now().plusHours(2))
                .end(LocalDateTime.now().plusHours(3))
                .status(Status.APPROVED)
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        when(repository.save(any(Booking.class))).thenReturn(booking);
        BookingDto bookingDto = toBookingDto((booking));
        bookingDto.setItemId(1);
        BookingDto resultBookingDto = service.create(2, bookingDto);
        booking.setId(1);
        resultBookingDto.setId(1);
        assertEquals(toBookingDto(booking), resultBookingDto);
        verify(repository, times(1)).save(any(Booking.class));
    }

    @Test
    void createWithItemNotAvailable() {
        Item item = Item.builder().available(false).build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        Exception e = assertThrows(NotAvailableException.class,
                () -> service.create(1, BookingDto.builder().itemId(1).build()));
        assertEquals("Вещь с ID: '1' не найдена", e.getMessage());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateStatus() {
        Item item = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .status(Status.REJECTED)
                .item(item)
                .booker(User.builder().id(2).build())
                .build();
        when(entityUtils.getBookingIfExists(anyInt())).thenReturn(booking);
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        when(repository.save(any(Booking.class))).thenReturn(booking);
        BookingDto actualBookingDto = service.updateStatus(1, 1, true);
        assertEquals(Status.APPROVED, actualBookingDto.getStatus());
        verify(entityUtils, times(1)).getBookingIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
        verify(repository, times(1)).save(any(Booking.class));
    }

    @Test
    void updateStatusWithApproveOnlyByItemOwner() {
        Item item = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        Booking booking = Booking.builder()
                .status(Status.REJECTED)
                .item(item)
                .build();
        when(entityUtils.getBookingIfExists(anyInt())).thenReturn(booking);
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        Exception e = assertThrows(NotFoundException.class,
                () -> service.updateStatus(2, 1, false));
        assertEquals("Пользователь с ID='2' не владеет вещью с ID='1'", e.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateStatusAlreadyApproved() {
        Booking booking = Booking.builder()
                .booker(User.builder().id(1).build())
                .status(Status.APPROVED)
                .item(Item.builder().id(1).build())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        when(entityUtils.getBookingIfExists(anyInt())).thenReturn(booking);
        var exception = assertThrows(IncorrectStatusUpdateException.class,
                () -> service.updateStatus(1, 1, true));
        assertEquals("Бронирование уже подтверждено", exception.getMessage());
        verify(entityUtils, times(1)).getBookingIfExists(anyInt());
    }

    private Booking createBooking() {
        return new Booking(1,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                createItem(),
                createUser(),
                Status.APPROVED);
    }

    private Item createItem() {
        return new Item(1, "name", "description", true, createUser(), createItemRequest());
    }

    private User createUser() {
        return new User(1, "name", "name@ya.com");
    }

    private ItemRequest createItemRequest() {
        return new ItemRequest(1, "description", createUser(), LocalDateTime.now());
    }
}