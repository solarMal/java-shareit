package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByBookerId(Integer id, Pageable pageable);

    @Query("select bc from Booking as bc where bc.booker.id = :bookerId and bc.start < :time and bc.end > :time order by bc.start desc")
    Page<Booking> findByBookerIdCurrenState(Integer bookerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsBefore(Integer bookerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfter(Integer bookerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStatus(Integer bookerId, Status status, Pageable pageable);

    Page<Booking> findBookingByItemOwnerId(Integer ownerId, Pageable pageable);

    @Query("select bc from Booking as bc where bc.item.owner.id = :ownerId and bc.start < :time and bc.end > :time order by bc.start asc")
    Page<Booking> findBookingByItemOwnerCurrentState(Integer ownerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndEndIsBefore(Integer ownerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndStartIsAfter(Integer ownerId, LocalDateTime time, Pageable pageable);

    Page<Booking> findBookingByItemOwnerIdAndStatus(Integer ownerId, Status status, Pageable pageable);

    List<Booking> findAllByItemIdAndBookerId(Integer itemId, Integer bookerId);

    List<Booking> findAllByItemId(Integer id);
}