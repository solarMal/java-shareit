package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void init() {
        owner = User.builder().name("owner").email("test1@mail.ru").build();
        item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        owner = userRepository.save(owner);
        item = itemRepository.save(item);
        booking = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(owner)
                .status(Status.APPROVED)
                .build();
        booking = bookingRepository.save(booking);
    }

    @Test
    void testFindAllByItemIdAndBookerId() {
        List<Booking> foundBookings = bookingRepository.findAllByItemIdAndBookerId(item.getId(), owner.getId());

        assertFalse(foundBookings.isEmpty());

        assertEquals(1, foundBookings.size());
    }

    @Test
    void testFindAllByItemId() {
        List<Booking> foundBookings = bookingRepository.findAllByItemId(item.getId());

        assertFalse(foundBookings.isEmpty());

        assertEquals(1, foundBookings.size());
    }

    @Test
    void testFindAllByBookerId() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerId(booking.getId());

        assertFalse(foundBookings.isEmpty());

        assertEquals(1, foundBookings.size());
    }

    @Test
    void testFindAllByItemOwnerId() {
        List<Booking> foundBookings = bookingRepository.findAllByItemOwnerId(owner.getId());

        assertFalse(foundBookings.isEmpty());

        assertEquals(1, foundBookings.size());
    }
}
