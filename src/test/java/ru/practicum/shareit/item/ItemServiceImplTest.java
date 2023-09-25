package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.entity.Comment;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnershipException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.item.dto.ItemMapper.toItem;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    EntityUtils entityUtils;
    @InjectMocks
    ItemServiceImpl service;

    @Test
    void findAllByUserId() {
        Item item1 = Item.builder().id(1).build();
        Item item2 = Item.builder().id(2).build();
        when(itemRepository.findAllByOwnerId(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(item1, item2)));
        List<ItemDto> result = service.findAllByUserId(1, 1, 1);
        ItemDto itemDto1 = toItemDto(item1);
        itemDto1.setComments(Collections.emptyList());
        ItemDto itemDto2 = toItemDto(item2);
        itemDto2.setComments(Collections.emptyList());
        assertArrayEquals(List.of(itemDto1, itemDto2).toArray(), result.toArray());
        verify(itemRepository, times(1)).findAllByOwnerId(anyInt(), any(Pageable.class));
    }

    @Test
    void findByUserIdWithComments() {
        Item item = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        Comment comment = Comment.builder()
                .id(1)
                .text("text")
                .created(LocalDateTime.of(2020, 1, 1, 0, 0))
                .item(Item.builder().id(1).build())
                .author(User.builder().id(1).name("name").build())
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .authorName("name")
                .text("text")
                .created(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
        when(commentRepository.findAllByItemIdOrderByCreated(anyInt())).thenReturn(List.of(comment));
        ItemDto result = service.findById(1, 1);
        assertArrayEquals(List.of(commentDto).toArray(), result.getComments().toArray());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
        verify(commentRepository, times(1)).findAllByItemIdOrderByCreated(anyInt());
    }

    @Test
    void findByUserIdWithoutComments() {
        Item item = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        ItemDto result = service.findById(1, 1);
        assertArrayEquals(Collections.emptyList().toArray(), result.getComments().toArray());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void findByUserIdWithBookings() {
        Booking prevBooking = Booking.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .status(Status.APPROVED)
                .id(1)
                .booker(User.builder().id(1).build())
                .build();
        Booking nextBooking = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .id(2)
                .booker(User.builder().id(2).build())
                .build();
        Item item = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        when(bookingRepository.findAllByItemId(anyInt())).thenReturn(List.of(prevBooking, nextBooking));
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        ItemDto result = service.findById(1, 1);
        assertEquals(ItemDto.NearBooking.builder()
                .id(1)
                .bookerId(1)
                .build(), result.getLastBooking());
        assertEquals(ItemDto.NearBooking.builder()
                .id(2)
                .bookerId(2)
                .build(), result.getNextBooking());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
        verify(bookingRepository, times(1)).findAllByItemId(anyInt());
    }

    @Test
    void findByUserIdWithoutBookings() {
        Item item = Item.builder()
                .owner(User.builder().id(1).build())
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        ItemDto resultItem = service.findById(1, 2);
        assertNull(resultItem.getLastBooking());
        assertNull(resultItem.getNextBooking());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void create() {
        ItemDto itemDto = new ItemDto();
        Item item = Item.builder().id(1)
                .owner(User.builder().id(1).build())
                .build();
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto createdItem = service.create(itemDto, item.getOwner().getId());
        assertEquals(toItem(createdItem, item.getOwner()).toString(), item.toString());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createWithStatus() {
        ItemDto itemDto = new ItemDto();
        Item item = Item.builder().id(1)
                .owner(User.builder().id(1).build())
                .request(ItemRequest.builder().id(1).build())
                .build();
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto createdItem = service.create(itemDto, item.getOwner().getId());
        assertEquals(toItem(createdItem, item.getOwner()).toString(), item.toString());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemName() {
        Item updatedItem = Item.builder()
                .owner(User.builder().id(1).build())
                .name("name")
                .build();
        ItemDto updatedDto = ItemDto.builder()
                .name("test")
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(updatedItem);
        ItemDto resultItem = service.update(updatedDto, 1, 1);
        assertEquals(updatedDto.getName(), resultItem.getName());
        assertEquals(updatedDto, resultItem);
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateItemDescription() {
        Item updatedItem = Item.builder()
                .owner(User.builder().id(1).build())
                .description("description")
                .build();
        ItemDto updatedDto = ItemDto.builder()
                .description("test")
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(updatedItem);
        ItemDto resultItem = service.update(updatedDto, 1, 1);
        assertEquals(updatedDto.getDescription(), resultItem.getDescription());
        assertEquals(updatedDto, resultItem);
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateItemAvailable() {
        Item updatedItem = Item.builder()
                .owner(User.builder().id(1).build())
                .available(false)
                .build();
        ItemDto updatedDto = ItemDto.builder()
                .available(true)
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(updatedItem);
        ItemDto resultItem = service.update(updatedDto, 1, 1);
        assertEquals(updatedDto.getAvailable(), resultItem.getAvailable());
        assertEquals(updatedDto, resultItem);
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateItemByNotItemOwner() {
        Item updatedItem = Item.builder()
                .id(1)
                .owner(User.builder().id(1).build())
                .build();
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(updatedItem);
        assertThrows(OwnershipException.class, () -> service.update(new ItemDto(), 1, 2));
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void updateNotExistsItem() {
        when(entityUtils.getItemIfExists(anyInt())).thenThrow(new NotFoundException("Вещь с ID='" + 1 + "' не найдена"));
        Exception e = assertThrows(NotFoundException.class, () -> service.update(new ItemDto(), 1, 1));
        assertEquals("Вещь с ID='1' не найдена", e.getMessage());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
    }

    @Test
    void findByText() {
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .owner(User.builder().id(2).build())
                .available(true)
                .build();
        when(itemRepository.findText(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemDto> result = service.findByText("description", 1, 1);
        assertNotNull(result);
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void findByEmptyText() {
        assertArrayEquals(Collections.emptyList().toArray(), service.findByText("", 1, 1).toArray());
    }

    @Test
    void addComment() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@ya.ru")
                .build();
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .owner(User.builder().id(2).build())
                .available(true)
                .build();
        Booking booking = Booking.builder()
                .id(1)
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2000, 1, 1, 0, 0))
                .end(LocalDateTime.of(2000, 1, 10, 0, 0))
                .build();
        Comment comment = Comment.builder()
                .id(1)
                .item(item)
                .author(user)
                .text("comment")
                .build();
        when(bookingRepository.findAllByItemIdAndBookerId(anyInt(), anyInt())).thenReturn(List.of(booking));
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(entityUtils.getItemIfExists(anyInt())).thenReturn(item);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentDto expected = CommentDto.builder()
                .id(1)
                .text("comment")
                .authorName(user.getName())
                .build();
        CommentDto actual = service.addComment(1, 1, expected);
        assertEquals(expected, actual);
        verify(bookingRepository, times(1)).findAllByItemIdAndBookerId(anyInt(), anyInt());
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(entityUtils, times(1)).getItemIfExists(anyInt());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentWithoutBooking() {
        when(bookingRepository.findAllByItemIdAndBookerId(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        assertThrows(NotAvailableException.class,
                () -> service.addComment(1, 1, CommentDto.builder().text("comment").build()));
    }
}