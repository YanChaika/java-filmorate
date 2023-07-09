package ru.yandex.practicum.filmorate.storage.film.likes;

import java.util.Set;

public interface LikesStorage {

    void create(int filmId, int userId);

    void remove(int filmId, int userId);

    void update(int filmId, int userId);

    Set<Integer> getSortedFilms();

    Set<Integer> getCommonFilmsId(Integer userId, Integer friendId);
}
