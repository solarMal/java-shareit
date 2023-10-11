package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDto> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable Integer id) {
        return service.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}
