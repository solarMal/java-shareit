package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.exception.IncorrectDateException;
import ru.practicum.shareit.exception.IncorrectStatusUpdateException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EntityUtils entityUtils;

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllByBookerAndState(Integer bookerId, State state, Integer from, Integer size) {
        entityUtils.getUserIfExists(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by("start").descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findByBookerId(bookerId, pageable).toList();
                break;
            case CURRENT:
                result = bookingRepository.findByBookerIdCurrenState(bookerId, now, pageable).toList();
                break;
            case PAST:
                result = bookingRepository.findByBookerIdAndEndIsBefore(bookerId, now, pageable).toList();
                break;
            case FUTURE:
                result = bookingRepository.findByBookerIdAndStartIsAfter(bookerId, now, pageable).toList();
                break;
            case WAITING:
                result = bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING, pageable).toList();
                break;
            case REJECTED:
                result = bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED, pageable).toList();
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllByOwnerAndState(Integer ownerId, State state, Integer from, Integer size) {
        entityUtils.getUserIfExists(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by("start").descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findBookingByItemOwnerId(ownerId, pageable).toList();
                break;
            case CURRENT:
                result = bookingRepository.findBookingByItemOwnerCurrentState(ownerId, now, pageable).toList();
                break;
            case PAST:
                result = bookingRepository.findBookingByItemOwnerIdAndEndIsBefore(ownerId, now, pageable).toList();
                break;
            case FUTURE:
                result = bookingRepository.findBookingByItemOwnerIdAndStartIsAfter(ownerId, now, pageable).toList();
                break;
            case WAITING:
                result = bookingRepository.findBookingByItemOwnerIdAndStatus(ownerId, Status.WAITING, pageable).toList();
                break;
            case REJECTED:
                result = bookingRepository.findBookingByItemOwnerIdAndStatus(ownerId, Status.REJECTED, pageable).toList();
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findById(Integer userId, Integer bookingId) {
        Booking booking = entityUtils.getBookingIfExists(bookingId);
        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException(String.format("Бронирование с ID: '%s' не найдено", bookingId));
        }
        return toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto create(Integer userId, BookingDto bookingDto) {
        User user = entityUtils.getUserIfExists(userId);
        Item item = entityUtils.getItemIfExists(bookingDto.getItemId());

        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();

        if (BooleanUtils.isFalse(item.getAvailable())) {
            throw new NotAvailableException(String.format("Вещь с ID: '%s' не найдена", bookingDto.getItemId()));
        }
        if (start == null || end == null) {
            throw new IncorrectDateException("Неверно задана дата бронирования");
        }
        if (start.isAfter(end) || start.equals(end)) {
            throw new IncorrectDateException("Дата начала позже или равна окончанию бронирования");
        }
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("Вещь не может быть забронирована владельцем");
        }
        return toBookingDto(bookingRepository.save(toBooking(bookingDto, user, item)));
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = entityUtils.getBookingIfExists(bookingId);
        Integer itemId = booking.getItem().getId();
        Item item = entityUtils.getItemIfExists(itemId);
        if (booking.getStatus() == Status.APPROVED && approved.equals(Boolean.TRUE)) {
            throw new IncorrectStatusUpdateException("Бронирование уже подтверждено");
        }
        if (Objects.equals(item.getOwner().getId(), userId)) {
            booking.setStatus(BooleanUtils.isTrue(approved) ? Status.APPROVED : Status.REJECTED);
        } else {
            throw new NotFoundException(String.format("Пользователь с ID='%s' не владеет вещью с ID='%s'", userId, itemId));
        }
        return toBookingDto(bookingRepository.save(booking));
    }
}