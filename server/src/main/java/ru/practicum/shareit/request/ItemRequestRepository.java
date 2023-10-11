package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    Page<ItemRequest> findAllByRequesterIdNot(Integer id, Pageable pageable);

    List<ItemRequest> findAllByRequesterId(Integer id, Sort sort);
}