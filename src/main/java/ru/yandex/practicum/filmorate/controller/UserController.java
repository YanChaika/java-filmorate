package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.UserAlreadyPresentException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static int countId = 1;


    @GetMapping("/users")
    public List<User> getUsers() {
        if (users.isEmpty()) {
            throw new ValidationException("Users is empty");
        }
        List<User> toReturn = new ArrayList<>();
        for (Integer integer : users.keySet()) {
            toReturn.add(users.get(integer));
        }
        log.trace("Amount of users" + users.size());
        return toReturn;
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!(user.getEmail().contains("@")) ||
                (user.getLogin().contains(" ")) ||
                (user.getBirthday().isAfter(LocalDate.now()))
        ) {
            throw new ValidationException("User can't be post");
        } else if (!users.containsKey(user.getId())) {
            user.setId(countId++);
            users.put(user.getId(), user);
            log.trace(user.toString());
            return user;
        } else {
            throw new UserAlreadyPresentException("User with email: " + user.getEmail() + " is exists");
        }
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!(user.getEmail().contains("@")) ||
                (user.getLogin().contains(" ")) ||
                (user.getBirthday().isAfter(LocalDate.now()))
        ) {
            throw new ValidationException("User can't be put");
        } else if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new UserAlreadyPresentException("User with email: " + user.getEmail() + " isn't exists");
        }
    }
}
