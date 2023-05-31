package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.UserAlreadyPresentException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

public UserController(UserStorage userStorage, UserService userService) {
    this.userStorage = userStorage;
    this.userService = userService;
}

    @GetMapping("/users")
    public List<User> getUsers() {
        return userStorage.getAll();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        checkIdOrThrowIfNullOrZeroOrLess(id);
        return userStorage.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User putFriendToUser(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        checkIdOrThrowIfNullOrZeroOrLess(id);
        checkIdOrThrowIfNullOrZeroOrLess(friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriendToUser(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        checkIdOrThrowIfNullOrZeroOrLess(id);
        checkIdOrThrowIfNullOrZeroOrLess(friendId);
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        checkIdOrThrowIfNullOrZeroOrLess(id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getAllFriendsCommonWithOther(
            @PathVariable Integer id,
            @PathVariable Integer otherId
    ) {
        checkIdOrThrowIfNullOrZeroOrLess(id);
        checkIdOrThrowIfNullOrZeroOrLess(otherId);
        return userService.compareFriends(id, otherId);
    }


    private void checkIdOrThrowIfNullOrZeroOrLess(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("id равен null");
        }
        if (id < 1) {
            throw new IncorrectIdException("id меньше 1");
        }
    }
}
