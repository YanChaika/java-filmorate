package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaController {

    private final FilmService filmService;

    @GetMapping("/mpa")
    public List<FilmMPA> getMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Optional<FilmMPA> getFilmMpaById(@PathVariable Integer id) {
        checkFilmIdOrThrowIfNullOrZeroOrLess(id);
        return filmService.getMpaById(id);
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
