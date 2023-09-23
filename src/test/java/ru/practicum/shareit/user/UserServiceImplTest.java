package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;
    @Mock
    private EntityUtils entityUtils;
    @InjectMocks
    private UserServiceImpl service;

    @Test
    void findAll() {
        User userOne = User.builder()
                .id(1)
                .name("userOne")
                .email("userOne@ya.com")
                .build();
        User userTwo = User.builder()
                .id(2)
                .name("userTwo")
                .email("userTwo@ya.com")
                .build();
        List<User> usersFromRepository = List.of(userOne, userTwo);

        when(repository.findAll()).thenReturn(usersFromRepository);

        assertArrayEquals(usersFromRepository.stream()
                .map(UserMapper::toUserDto).toArray(), service.findAll().toArray());

        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@ya.com")
                .build();
        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        long expected = List.of(user).size();
        long actual = repository.findById(1).stream().count();
        assertEquals(expected, actual);
    }


    @Test
    void create() {
        var userDto = new UserDto();
        var user = User.builder().id(1).build();
        when(repository.save(any(User.class))).thenReturn(user);
        var createdUser = service.create(userDto);
        assertEquals(user.getId(), createdUser.getId());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void updateNameNullAndEmailNull() {
        UserDto userDto = new UserDto();
        User updatedUser = User.builder()
                .id(1)
                .name("name")
                .email("name@ya.com")
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(updatedUser);
        when(repository.save(any(User.class))).thenReturn(updatedUser);
        assertEquals(toUserDto(updatedUser), service.update(userDto, 1));
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void updateNameNonNullAndEmailNull() {
        UserDto userDto = UserDto.builder()
                .name("test")
                .build();
        User user = User.builder()
                .id(1)
                .name("name")
                .email("name@ya.com")
                .build();
        User updatedUser = User.builder()
                .id(user.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(updatedUser);
        assertEquals(toUserDto(updatedUser), service.update(userDto, 1));
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void updateNameNullAndEmailNonNullAndNotDuplicate() {
        UserDto userDto = UserDto.builder()
                .email("test@ya.com")
                .build();
        User user = User.builder()
                .id(1)
                .name("name")
                .email("name@ya.com")
                .build();
        User updatedUser = User.builder()
                .id(user.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(updatedUser);
        assertEquals(toUserDto(updatedUser), service.update(userDto, 1));
        verify(entityUtils, times(1)).getUserIfExists(anyInt());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void updateNameNullAndEmailDuplicate() {
        var userDto = UserDto.builder()
                .email("test@ya.com")
                .build();
        var user = new User();
        var sameUser = User.builder()
                .id(2)
                .name("name")
                .email("test@ya.com")
                .build();
        when(entityUtils.getUserIfExists(anyInt())).thenReturn(user);
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(sameUser));
        assertThrows(AlreadyExistException.class, () -> service.update(userDto, 1));
    }

    @Test
    void deleteById() {
        int id = 1;
        service.deleteById(id);
        verify(repository).deleteById(id);
    }
}