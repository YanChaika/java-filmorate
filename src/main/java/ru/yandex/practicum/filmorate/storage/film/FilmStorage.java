package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    void delete(Integer id);

    Film getFilmById(Integer id);

    List<Film> getSortedFilms();

    List<Film> getFilmsByDirectorSortedByYear(int directorId);

    List<Film> getFilmsByDirectorSortLikes(int directorId);

    List<Film> getTwoUsersCommonFilms(Integer userId, Integer friendId);
}
