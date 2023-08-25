package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItem = itemService.createItem(userId, itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto updatedItem) {
        ItemDto updatedItemDto = itemService.updateItem(itemId, userId, updatedItem);
        return ResponseEntity.ok(updatedItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long itemId) {
        ItemDto itemDto = itemService.getItemById(itemId);

        if (itemDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + itemId + " not found");
        }

        return ResponseEntity.ok(itemDto);
    }

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> itemsByOwner = itemService.getItemsByOwner(userId);
        return ResponseEntity.ok(itemsByOwner);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItemsByText(
            @RequestParam("text") String searchText,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> matchingItems = itemService.searchItemsByText(searchText, userId);
        return ResponseEntity.ok(matchingItems);
    }

}
