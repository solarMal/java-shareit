package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Integer id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Integer id);

    void deleteById(Integer id);
}