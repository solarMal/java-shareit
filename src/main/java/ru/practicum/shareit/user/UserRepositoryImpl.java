package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.usererror.InvalidEmailException;
import ru.practicum.shareit.user.usererror.UserAlreadyExistException;
import ru.practicum.shareit.user.usererror.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final List<UserDto> users = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public UserDto save(UserDto user) {
        if (isEmailAlreadyUsed(user.getEmail())) {
            log.warn("Пользователь с такой электронной почтой уже существует");
            throw new InvalidEmailException("Пользователь с такой электронной почтой уже существует");
        }

        if (user.getId() == null) {
            user.setId(nextId++);
        }
        log.info("user сохранён " + user);
        users.add(user);
        return user;
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("найден user" + users);
        return users;
    }

    @Override
    public UserDto getUserById(Long id) {
        for (UserDto user : users) {
            if (user.getId().equals(id)) {
                log.info("найден user" + user);
                return user;
            }
        }
        return null;
    }


    @Override
    public UserDto updateUser(Long id, UserDto updatedUser) {
        UserDto existingUser = getUserById(id);

        if (existingUser == null) {
            throw new UserAlreadyExistException("User with id " + id + " not found");
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && isEmailAlreadyTaken(updatedUser.getEmail())) {
            throw new UserAlreadyExistException("User with email " + updatedUser.getEmail() + " already exists");
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        log.info("user обновлён " + existingUser);

        int index = users.indexOf(existingUser);
        if (index >= 0) {
            users.set(index, existingUser);
        }

        return existingUser;
    }


    @Override
    public void deleteUserById(int id) {
        UserDto userToRemove = null;
        for (UserDto user : users) {
            if (user.getId() == id) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            users.remove(userToRemove);
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    private boolean isEmailAlreadyUsed(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    private boolean isEmailAlreadyTaken(String email) {
        for (UserDto user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
