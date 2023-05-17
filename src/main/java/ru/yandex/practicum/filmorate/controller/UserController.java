package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.UserAlreadyException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static int countId = 1;


    @GetMapping("/users")
    public User[] getUsers() {
        if (users.isEmpty()) {
            throw new ValidationException("Users is empty");
        }
        int i = 0;
        User[] toReturn = new User[users.size()];
        for (Integer integer : users.keySet()) {
            toReturn[i++] = users.get(integer);
        }
        log.trace("Amount of users" + users.size());
        return toReturn;
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        if ((user.getEmail() != null)&&(user.getEmail().contains("@"))) {
            if ((user.getLogin() != null)&&(!user.getLogin().contains(" "))) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                } if (user.getBirthday().isBefore(LocalDate.now())) {
                    if (!users.containsKey(user.getId())) {
                        user.setId(countId++);
                        users.put(user.getId(), user);
                        log.trace(user.toString());
                        return user;
                    } else {
                        throw new UserAlreadyException("User with email: " + user.getEmail() + " is exists");
                    }
                }
            }
        }
        throw new ValidationException("User can't be post");
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        if ((user.getEmail() != null)&&(user.getEmail().contains("@"))) {
            if ((user.getLogin() != null)&&(!user.getLogin().contains(" "))) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                } if (user.getBirthday().isBefore(LocalDate.now())){
                    if (users.containsKey(user.getId())) {
                        users.put(user.getId(), user);
                        return user;
                    } else {
                        throw new UserAlreadyException("User with email: " + user.getEmail() + " is'nt exists");
                    }
                }
            }
        }
        throw new ValidationException("User can't be put");
    }
}
