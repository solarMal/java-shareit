package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.debug("запрос на добавление нового пользователя.");

        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") long userId,
                              @RequestBody UserDto userDto) {
        log.debug("запрос на обновление нового пользователя. {}.", userId);

        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable(value = "userId") long userId) {
        log.debug("запрос пользователя. {}.", userId);

        return userService.getUserDtoById(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.debug("запрос всех пользователей.");

        return userService.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(value = "userId") long userId) {
        log.debug("запрос на удаление user {}.", userId);

        userService.deleteUser(userId);

    }
}
