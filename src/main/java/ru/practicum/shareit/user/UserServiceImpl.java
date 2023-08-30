package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;


    @Override
    public UserDto save(UserDto userDto) {
        return repository.save(userDto);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return repository.findAllUsers();
    }

    @Override
    public UserDto getUserById(Long id) {
        return repository.getUserById(id);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return repository.updateUser(id, userDto);
    }

    @Override
    public void deleteUserById(int id) {
        repository.deleteUserById(id);
    }
}
