package ru.yandex.practicum.filmorate.storage.film.genres;

import ru.yandex.practicum.filmorate.model.FilmByGenres;

import java.util.List;

public interface GenresStorage {

    FilmByGenres create(FilmByGenres filmByGenres);

    void remove(int id);

    List<FilmByGenres> getAll();

    List<FilmByGenres> getGenreById(int id);

}
