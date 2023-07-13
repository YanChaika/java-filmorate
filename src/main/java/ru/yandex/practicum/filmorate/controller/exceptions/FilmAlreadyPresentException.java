package ru.yandex.practicum.filmorate.controller.exceptions;

public class FilmAlreadyPresentException extends RuntimeException {

    public FilmAlreadyPresentException(final String message) {
        super(message);
    }
}
