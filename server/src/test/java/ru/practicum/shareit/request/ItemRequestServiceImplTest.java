package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequest;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private EntityUtils entityUtils;
    @InjectMocks
    private ItemRequestServiceImpl service;
    private User user;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1)
                .name("name")
                .email("email@mail.ru")
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void findAllByUserId() {
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(requestRepository.findAllByRequesterId(anyInt(), any(Sort.class)))
                .thenReturn(List.of(toItemRequest(itemRequestDto)));
        Assertions.assertEquals(1, service.findAllByUserId(1).size());
        Assertions.assertArrayEquals(List.of(itemRequestDto).toArray(),
                service.findAllByUserId(1).toArray());
        verify(requestRepository, times(2)).findAllByRequesterId(anyInt(), any(Sort.class));
    }

    @Test
    void findById() {
        when(itemRepository.findAllByRequestId(anyInt())).thenReturn(Collections.emptyList());
        when(entityUtils.getItemRequestIfExists(anyInt()))
                .thenReturn(toItemRequest(itemRequestDto));
        Assertions.assertEquals(itemRequestDto, service.findById(1, 1));
    }

    @Test
    void findAllByParams() {
        when(itemRepository.findAllByRequestId(anyInt())).thenReturn(Collections.emptyList());
        when(requestRepository.findAllByRequesterIdNot(anyInt(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(toItemRequest(itemRequestDto))));
        Assertions.assertEquals(List.of(itemRequestDto), service.findAllByParams(1, PageRequest.of(0, 2, Sort.by("created"))));
    }

    @Test
    void create() {
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(toItemRequest(itemRequestDto));
        Assertions.assertEquals(itemRequestDto, service.create(itemRequestDto, 1));
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }
}