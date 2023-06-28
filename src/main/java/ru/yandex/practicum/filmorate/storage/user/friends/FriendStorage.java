package ru.yandex.practicum.filmorate.storage.user.friends;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    User create(User user, User friend);

    void remove(User user, User friend);

    List<Integer> getAllById(int id);
}
