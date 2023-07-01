package ru.yandex.practicum.filmorate.storage.film.mpa;

import ru.yandex.practicum.filmorate.model.FilmMPA;

import java.util.List;

public interface MpaStorage {

    List<FilmMPA> getAllMpa();

    FilmMPA getMpaById(int id);
}
