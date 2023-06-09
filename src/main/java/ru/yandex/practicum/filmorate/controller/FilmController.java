package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

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

    @DeleteMapping("/films/{filmId}")
    public void removeFilm(@PathVariable Integer filmId) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(filmId);
        filmService.removeFilm(filmId);
    }

    @GetMapping("/films/director/{directorId}")
    public List<Film> getByDirectorId(
            @PathVariable Integer directorId,
            @RequestParam(required = false) String sortBy
    ) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(directorId);
        return filmService.getByDirectorId(directorId, sortBy);
    }

    @GetMapping("/films/popular")
    public List<Film> getCountPopularFilmByLikes(
            @RequestParam(defaultValue = "10") @Positive Integer count,
            @RequestParam(defaultValue = "0") Integer genreId,
            @RequestParam(defaultValue = "0") Integer year
    ) {
        return filmService.getSortedFilmsByGenreAndYear(count, genreId, year);

    }

    @GetMapping("/films/common")
    public List<Film> getTwoUsersCommonFilms(
            @RequestParam Integer userId,
            @RequestParam Integer friendId) {
        return filmService.getTwoUsersCommonFilms(userId, friendId);
    }

    @GetMapping("/films/search")
    public List<Film> searchFilms(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "director,title", required = false) String by) {
        log.info("Поиск фильма по " + by + ", запрос: " + query);
        if (query == null) {
            String message = "Пустой запрос поиска фильмов.";
            log.info(message);
            throw new RuntimeException(message);
        }
        return filmService.searchFilms(query, by);
    }

    private void checkFilmIdOrThrowIfNullOrZeroOrLess(Integer id) {
        if (id == null) {
            throw new IncorrectIdException("id равен null");
        }
        if (id < 1) {
            throw new IncorrectIdException("id меньше 1");
        }
    }
}