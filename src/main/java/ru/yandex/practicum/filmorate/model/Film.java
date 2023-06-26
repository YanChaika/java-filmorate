package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {

    private int id;
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
    private List<Genre> genres;
    private FilmMPA mpa;

    public Film(int id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration,
                FilmMPA mpa
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film asCreated(int id) {
        return new Film(id, name, description, releaseDate, duration, mpa);
    }
}
