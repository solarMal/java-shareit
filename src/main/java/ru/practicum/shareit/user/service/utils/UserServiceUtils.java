package ru.practicum.shareit.user.service.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.model.InvalidEmailException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

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

    public void checkEmailForValid(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new InvalidEmailException("Email address is required");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (!userDto.getEmail().matches(emailRegex)) {
            throw new InvalidEmailException("Invalid email address");
        }
    }


    public User convertToUser(UserDto userDto) {
        return UserMapper.convertToUser(userDto);
    }

    public UserDto convertToDto(User user) {
        return UserMapper.convertToDto(user);
    }
}
