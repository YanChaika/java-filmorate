package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.UserAlreadyPresentException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private static int countId = 1;

    @Override
    public List<User> getAll() {
        if (users.isEmpty()) {
            throw new IncorrectIdException("Users is empty");
        }
        List<User> toReturn = new ArrayList<>();
        for (Integer integer : users.keySet()) {
            toReturn.add(users.get(integer));
        }
        log.trace("Amount of users" + users.size());
        return toReturn;
    }

    @Override
    public User create(User user) {
        if ((user.getName() == null)||(user.getName().isBlank())) {
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

    @Override
    public User update(User user) {
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

    @Override
    public User delete(User user) {
        return null;
    }

    @Override
    public User getById(Integer id) {
        if (!users.containsKey(id)) {
            throw new IncorrectIdException("user c id н найден");
        }
        return users.get(id);
    }
}
