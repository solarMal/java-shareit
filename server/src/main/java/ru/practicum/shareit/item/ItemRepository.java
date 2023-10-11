package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findAllByOwnerId(Integer id, Pageable pageable);

    @Query("select item from Item as item where lower(item.name) like lower(concat('%', :text, '%')) " +
            "or lower(item.description) like lower(concat('%', :text, '%')) and item.available = true")
    Page<Item> findText(String text, Pageable pageable);

    List<Item> findAllByRequestId(Integer id);
}