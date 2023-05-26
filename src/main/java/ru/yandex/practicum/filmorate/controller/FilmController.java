package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.FilmAlreadyException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    private static int countId = 1;


    @GetMapping("/films")
    public Film[] getFilms() {
        if (films.isEmpty()) {
            throw new ValidationException("films is empty");
        }
        int i = 0;
        Film[] toReturn = new Film[films.size()];
        for (Integer integer : films.keySet()) {
            toReturn[i++] = films.get(integer);
        }
        log.trace("Amount of films" + films.size());
        return toReturn;
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be post film");
        } else if (!films.containsKey(film.getId())) {
            film.setId(countId++);
            films.put(film.getId(), film);
            log.trace(film.toString());
            return film;
        } else {
            throw new FilmAlreadyException("Film with name: " + film.getName() + " is exists");
        }
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be put film");
        } else if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new FilmAlreadyException("Film with name: " + film.getName() + " don't exists");
        }
    }
}
