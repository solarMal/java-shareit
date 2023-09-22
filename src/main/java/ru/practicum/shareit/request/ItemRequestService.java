package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDto> findAllByUserId(Integer userId);

    ItemRequestDto findById(Integer userId, Integer requestId);

    List<ItemRequestDto> findAllByParams(Integer userId, Pageable pageable);

    ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId);
}