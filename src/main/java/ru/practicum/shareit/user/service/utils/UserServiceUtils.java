package ru.practicum.shareit.user.service.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


@Component
public class UserServiceUtils {
    public void checkIsUserValid(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new ValidationException("Name is blank");
        }
    }

    public void checkNameForUpdating(UserDto userDto) {
        if (userDto.getName().isBlank()) {
            throw new ValidationException("Name is blank");
        }
    }


    public User convertToUser(UserDto userDto) {
        return UserMapper.convertToUser(userDto);
    }

    public ru.practicum.shareit.user.dto.UserDto convertToDto(User user) {
        return UserMapper.convertToDto(user);
    }
}
