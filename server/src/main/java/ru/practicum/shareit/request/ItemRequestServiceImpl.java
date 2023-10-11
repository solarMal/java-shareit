package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequestDto;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final EntityUtils entityUtils;


    @Override
    public List<ItemRequestDto> findAllByUserId(Integer userId) {
        entityUtils.getUserIfExists(userId);
        Sort sort = Sort.by("created");
        return itemRequestRepository.findAllByRequesterId(userId, sort).stream()
                .map(itemRequest -> toItemRequestDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findById(Integer userId, Integer requestId) {
        entityUtils.getUserIfExists(userId);
        ItemRequest itemRequest = entityUtils.getItemRequestIfExists(requestId);
        List<ItemDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return toItemRequestDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestDto> findAllByParams(Integer userId, Pageable pageable) {
        return itemRequestRepository.findAllByRequesterIdNot(userId, pageable).stream()
                .map(itemRequest -> toItemRequestDto(itemRequest, getItemDtoListByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    private List<ItemDto> getItemDtoListByRequestId(Integer requestId) {
        return itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Integer userId) {
        User user = entityUtils.getUserIfExists(userId);
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        itemRequest.setRequester(user);
        ItemRequest itemRequestFromRepository = itemRequestRepository.save(itemRequest);
        return toItemRequestDto(itemRequestFromRepository, null);
    }
}