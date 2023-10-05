package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.utils.EntityUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.toUser;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityUtils entityUtils;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        return toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID: '%s' не найден", id))));
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, Integer id) {
        User updatedUser = entityUtils.getUserIfExists(id);
        String userDtoName = userDto.getName();
        String userDtoEmail = userDto.getEmail();
        if (StringUtils.isNotBlank(userDtoName)) {
            updatedUser.setName(userDtoName);
        }
        if (StringUtils.isNotBlank(userDtoEmail)) {
            checkEmail(userDtoEmail, id);
            updatedUser.setEmail(userDtoEmail);
        }
        return toUserDto(userRepository.save(updatedUser));
    }

    private void checkEmail(String email, Integer id) {
        Optional<UserDto> nUser = userRepository.findByEmail(email)
                .map(UserMapper::toUserDto);
        if (nUser.isPresent()
                && nUser.get().getEmail().equals(email)
                && !Objects.equals(nUser.get().getId(), id)) {
            throw new AlreadyExistException(String.format("Email: '%s' уже существует", email));
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}