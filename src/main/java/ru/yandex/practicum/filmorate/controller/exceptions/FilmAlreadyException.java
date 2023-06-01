package ru.yandex.practicum.filmorate.controller.exceptions;

public class FilmAlreadyException extends RuntimeException {

    public FilmAlreadyException(final String message) {
        super(message);
    }
}
