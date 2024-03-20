package ru.practicum.explore.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explore.db.Dao;
import ru.practicum.explore.model.exceptions.ConflictException;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final Dao dao;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        var userToCreate = userMapper.fromDto(userDto);
        User createdUser;
        try {
            createdUser = dao.createUser(userToCreate);
            return userMapper.toDto(createdUser);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("duplicate");
        }
    }

    public void deleteUserById(long userId) {
        dao.deleteUserById(userId);
    }

    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids.isEmpty()) {
            return dao.getAllUsersPage(from, size).stream().map(userMapper::toDto).collect(Collectors.toList());
        } else {
            return dao.getUsersByIdIn(ids).stream().map(userMapper::toDto).collect(Collectors.toList());
        }
    }
}
