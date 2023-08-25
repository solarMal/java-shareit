package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final List<ItemDto> items = new ArrayList<>();
    private long nextId = 1;

    @Override
    public ItemDto save(ItemDto item) {
        if (item.getId() == 0) {
            item.setId(nextId++);
        }

        items.add(item);
        return item;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        int index = items.indexOf(itemDto);
        if (index >= 0) {
            items.set(index, itemDto);
        }
        log.info("item изменён " + itemDto);
        return itemDto;
    }

    @Override
    public ItemDto getItemById(Long id) {
        for (ItemDto itemDto : items) {
            if (itemDto.getId() == id) {
                log.info("найден item" + itemDto);
                return itemDto;
            }
        }
        return null;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return items;
    }


}
