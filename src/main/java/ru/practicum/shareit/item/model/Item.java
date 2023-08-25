package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private UserDto owner;
    private Long request;
    private Boolean available;
}
