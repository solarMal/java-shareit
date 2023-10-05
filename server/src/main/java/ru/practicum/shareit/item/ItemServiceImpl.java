package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.entity.Comment;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.OwnershipException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.comment.dto.CommentMapper.toComment;
import static ru.practicum.shareit.comment.dto.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final EntityUtils entityUtils;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findAllByUserId(Integer userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemRepository.findAllByOwnerId(userId, pageable).toList();
        return items.stream()
                .map(ItemMapper::toItemDto)
                .map(this::addCommentsDto)
                .map(this::addBookings)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(Integer itemId, Integer userId) {
        Item item = entityUtils.getItemIfExists(itemId);
        ItemDto itemDto = toItemDto(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            addBookings(itemDto);
        }
        addCommentsDto(itemDto);
        return itemDto;
    }

    private ItemDto addCommentsDto(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreated(itemDto.getId());
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    private ItemDto addBookings(ItemDto itemDto) {
        List<Booking> bookings = bookingRepository.findAllByItemId(itemDto.getId());
        Booking lastBooking = bookings.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStatus() == Status.APPROVED)
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
        itemDto.setLastBooking(lastBooking != null ? ItemDto.NearBooking.builder()
                .id(lastBooking.getId())
                .bookerId(lastBooking.getBooker().getId())
                .build() : null);
        itemDto.setNextBooking(nextBooking != null ? ItemDto.NearBooking.builder()
                .id(nextBooking.getId())
                .bookerId(nextBooking.getBooker().getId())
                .build() : null);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Integer userId) {
        var user = entityUtils.getUserIfExists(userId);
        if (itemDto.getRequestId() != null) {
            var request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow();
            var item = toItem(itemDto, user);
            item.setRequest(request);
            return toItemDto(itemRepository.save(item));
        }
        return toItemDto(itemRepository.save(toItem(itemDto, entityUtils.getUserIfExists(userId))));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Integer itemId, Integer userId) {
        Item updatedItem = entityUtils.getItemIfExists(itemId);
        if (Optional.ofNullable(updatedItem.getOwner()).isPresent()
                && !Objects.equals(updatedItem.getOwner().getId(), userId)) {
            throw new OwnershipException(String.format("Пользователь с ID='%s' не владеет вещью с ID='%s'", userId, itemId));
        }
        Optional.ofNullable(itemDto.getName()).ifPresent(updatedItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updatedItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updatedItem::setAvailable);
        updatedItem.setOwner(entityUtils.getUserIfExists(userId));
        itemRepository.save(updatedItem);
        return toItemDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findByText(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            List<Item> items = itemRepository.findText(text, pageable).toList();
            return items.stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer userId, Integer itemId, CommentDto commentDto) {
        if (isBookingExists(userId, itemId)) {
            throw new NotAvailableException(String.format("Пользователь с ID='%s' не бронировал вещь с ID='%s'", userId, itemId));
        }
        Comment comment = toComment(commentDto);
        comment.setAuthor(entityUtils.getUserIfExists(userId));
        comment.setItem(entityUtils.getItemIfExists(itemId));
        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }

    private boolean isBookingExists(Integer userId, Integer itemId) {
        return bookingRepository.findAllByItemIdAndBookerId(itemId, userId).stream()
                .noneMatch(booking -> booking.getStatus() == Status.APPROVED
                        && booking.getStart().isBefore(LocalDateTime.now()));
    }
}