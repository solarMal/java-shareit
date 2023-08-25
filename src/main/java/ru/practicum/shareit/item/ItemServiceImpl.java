package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        UserDto owner = userRepository.getUserById(userId);

        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
        }

        if (itemDto.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
        }

        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }

        itemDto.setOwner(owner);

        return itemRepository.save(itemDto);
    }

    public ItemDto updateItem(Long itemId, Long userId, ItemDto updatedItem) {
        ItemDto existingItem = itemRepository.getItemById(itemId);

        if (existingItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + itemId + " not found");
        }

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the item");
        }

        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }

        if (updatedItem.getDescription() != null) {
            existingItem.setDescription(updatedItem.getDescription());
        }

        if(updatedItem.getAvailable() != null){
            existingItem.setAvailable(updatedItem.getAvailable());
        }

        return itemRepository.updateItem(existingItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        ItemDto itemDto = itemRepository.getItemById(itemId);

        if (itemDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + itemId + " not found");
        }

        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        List<ItemDto> itemsByOwner = new ArrayList<>();

        for (ItemDto itemDto : itemRepository.getAllItems()) {
            if (itemDto.getOwner().getId().equals(ownerId)) {
                itemsByOwner.add(itemDto);
            }
        }

        return itemsByOwner;
    }

    @Override
    public List<ItemDto> searchItemsByText(String searchText, Long userId) {
        List<ItemDto> matchingItems = new ArrayList<>();
        String searchLower = searchText.toLowerCase();

        if (searchLower.isEmpty()) {
            return matchingItems;
        }

        for (ItemDto itemDto : itemRepository.getAllItems()) {
            if (itemDto.getAvailable() &&
                    (itemDto.getName().toLowerCase().contains(searchLower) ||
                            itemDto.getDescription().toLowerCase().contains(searchLower))) {
                matchingItems.add(itemDto);
            }
        }

        return matchingItems;
    }

}
