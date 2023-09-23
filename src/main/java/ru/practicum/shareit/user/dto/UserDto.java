package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer id;

    @NotBlank(message = "Имя пользователя не указано")
    private String name;

    @NotBlank(message = "Email пользователя не указан")
    @Email(message = "Неверный формат email")
    private String email;
}