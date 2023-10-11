package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private User owner;
    private User booker;
    private Booking booking;

    @BeforeEach
    void init() {
        owner = User.builder().name("owner").email("test1@mail.ru").build();
        booker = User.builder().name("booker").email("test2@mail.ru").build();
        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        owner = userRepository.save(owner);
        booker = userRepository.save(booker);
        item = itemRepository.save(item);
        booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();
        booking = bookingRepository.save(booking);
    }

    @Test
    void findByBookerIdCurrentState() {
        Page<Booking> result = bookingRepository.findByBookerIdCurrenState(
                booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemOwnerCurrentState() {
        Page<Booking> result = bookingRepository.findBookingByItemOwnerCurrentState(
                owner.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertEquals(booking, result.getContent().get(0));
    }
}