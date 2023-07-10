package ru.yandex.practicum.filmorate.controller.exceptions;

public class IncorrectIdException extends RuntimeException {

    public IncorrectIdException(String message) {
        super(message);
    }

}
