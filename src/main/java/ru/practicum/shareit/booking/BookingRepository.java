package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByItemIdAndBookerId(Integer itemId, Integer bookerId);

    List<Booking> findAllByItemId(Integer itemId);

    List<Booking> findAllByBookerId(Integer bookerId);

    List<Booking> findAllByItemOwnerId(Integer ownerId);
}