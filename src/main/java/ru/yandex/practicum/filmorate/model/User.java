package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User asCreated(int id) {
        return new User(id, email, login, name, birthday);
    }

}

