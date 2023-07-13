package ru.yandex.practicum.filmorate.storage.film.director;

import ru.yandex.practicum.filmorate.model.FilmDirectorRelation;

import java.util.List;

public interface FilmsDirectorsRelationStorage {

    List<FilmDirectorRelation> getByFilmId(int id);

    void deleteByFilmId(int id);

    List<FilmDirectorRelation> getByDirectorId(int id);

}
