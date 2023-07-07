package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    private final int id;
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
    private List<Genre> genres;
    private final FilmMPA mpa;
    private Set<Director> directors;

    public Film asCreated(int id) {
        Film film = new Film(id, name, description, releaseDate, duration, mpa);
        film.setDirectors(directors);
        return film;
    }
}
