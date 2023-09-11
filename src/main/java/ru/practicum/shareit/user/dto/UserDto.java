package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    long id;

    @NotBlank
    private final String name;

    @NotBlank
    String email;
}
