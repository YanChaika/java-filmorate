package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.FilmMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void postLikeToFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        checkFilmIdOrThrowIfNullOrZeroOrLess(userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLikeFromFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        checkFilmIdOrThrowIfNullOrZeroOrLess(userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getCountPopularFilmByLikes(
            @RequestParam(required = false) String count) {
        int countFilmsByLikes;
        if (count == null) {
            countFilmsByLikes = 10;
        } else {
        countFilmsByLikes = Integer.parseInt(count);
        }
        return filmService.getCountFilmsByLike(countFilmsByLikes);
    }

    private void checkFilmIdOrThrowIfNullOrZeroOrLess(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("id равен null");
        }
        if (id < 1) {
            throw new IncorrectIdException("id меньше 1");
        }
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getFilmGenreById(@PathVariable Integer id) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<FilmMPA> getMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Optional<FilmMPA> getFilmMpaById(@PathVariable Integer id) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        return filmService.getMpaById(id);
    }
}
