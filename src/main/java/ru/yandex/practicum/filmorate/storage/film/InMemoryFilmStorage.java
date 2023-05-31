package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.FilmAlreadyException;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    private static int countId = 1;
    /*private Set<Film> filmsByLikes =  new TreeSet<>(new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
        }
    });*/

    @Override
    public List<Film> getAll() {
        if (!films.isEmpty()) {
            //throw new IncorrectIdException("films is empty");
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

    @Override
    public Film update(Film film) {
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
    public Set<Film> getSortedFilms() {
        Set<Film> filmsByLikes;
        if (!films.isEmpty()) {
            filmsByLikes = new TreeSet<>(new Comparator<Film>() {
                @Override
                public int compare(Film o1, Film o2) {
                    return Integer.compare(o2.getCountLikes(), o1.getCountLikes());
                }
            });
            for (Integer integer : films.keySet()) {
                filmsByLikes.add(films.get(integer));
            }
            return filmsByLikes;
        }
        return new HashSet<>();
    }
}
