package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer id, Integer userId) {
        Film filmToUpdate = filmStorage.getFilmById(id);
        if (filmToUpdate == null) {
            throw new IncorrectIdException("Film для добавления лайка не найден");
        }
        Set<Integer> likesByFilm = filmToUpdate.getLikes();
        if (likesByFilm == null) {
            likesByFilm = new HashSet<>();
        }
        likesByFilm.add(userId);
        filmToUpdate.setLikes(likesByFilm);
        filmToUpdate.setCountLikes(filmToUpdate.getLikes().size());
        return filmStorage.update(filmToUpdate);
    }

    public Film removeLike(Integer id, Integer userId) {
        try {
            Film filmToUpdate = filmStorage.getFilmById(id);
            Set<Integer> likesByFilm = filmToUpdate.getLikes();
            likesByFilm.remove(userId);
            return filmStorage.update(filmToUpdate);
        } catch (NullPointerException e) {
            throw new IncorrectIdException("Film для удаления не найден");
        }
    }

    public List<Film> getCountFilmsByLike(Integer count) {
        List<Film> sortedFilms = new ArrayList<>(filmStorage.getSortedFilms());
        List<Film> countBySortedFilms = new ArrayList<>();
        if (!sortedFilms.isEmpty()) {
            int minOfCountAndSizeOfSortedFilms;
            if (sortedFilms.size() < count) {
                minOfCountAndSizeOfSortedFilms = sortedFilms.size();
            } else {
                minOfCountAndSizeOfSortedFilms = count;
            }
            for (int i = 0; i < minOfCountAndSizeOfSortedFilms; i++)
                countBySortedFilms.add(sortedFilms.get(i));
        }
        return countBySortedFilms;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be post film");
        } else {
            return filmStorage.create(film);
        }
    }

    public Film update(Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be put film");
        } else {
            return filmStorage.update(film);
        }
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }
}
