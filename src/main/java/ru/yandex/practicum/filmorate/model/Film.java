package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

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

    public Film asCreated(int id) {
        return new Film(id, name, description, releaseDate, duration, mpa);
    }
}
