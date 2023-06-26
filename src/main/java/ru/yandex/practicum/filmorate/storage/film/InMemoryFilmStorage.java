package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.FilmAlreadyException;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int countId = 1;

    @Override
    public List<Film> getAll() {
        if (!films.isEmpty()) {
            int i = 0;
            List<Film> toReturn = new ArrayList<>();
            for (Integer integer : films.keySet()) {
                toReturn.add(films.get(integer));
            }
            log.trace("Amount of films" + films.size());
            return toReturn;
        }
        return new ArrayList<>();
    }

    @Override
    public Film create(Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(countId++);
            films.put(film.getId(), film);
            log.trace(film.toString());
            return film;
        } else {
            throw new FilmAlreadyException("Film with name: " + film.getName() + " is exists");
        }
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new FilmAlreadyException("Film with name: " + film.getName() + " don't exists");
        }
    }

    @Override
    public Film delete(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new IncorrectIdException("Film with id " + id + " not found");
        }
    }

    @Override
    public List<Film> getSortedFilms() {

        return new ArrayList<>();
    }
}
