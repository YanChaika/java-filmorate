package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserToCreate {

    private final int id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;

    public User asCreated(int id) {
        return new User(id, email, login, name, birthday);
    }
}
