package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getFilmGenreById(@PathVariable Integer id) {
        checkFilmIdOrThrowIfZeroOrLess(id);
        return filmService.getGenreById(id);
    }

    private void checkFilmIdOrThrowIfZeroOrLess(Integer id) {
        if (id < 1) {
            throw new IncorrectIdException("id меньше 1");
        }
    }
}
