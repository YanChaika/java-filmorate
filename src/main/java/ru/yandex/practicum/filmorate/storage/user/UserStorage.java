package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface UserStorage {
    List<User> getAll();

    User create(User user);

    User update(User user);

    User delete(User user);

    Optional<User> getById(Integer id);
}
