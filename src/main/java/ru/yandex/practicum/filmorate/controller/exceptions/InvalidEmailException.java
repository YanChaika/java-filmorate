package ru.yandex.practicum.filmorate.controller.exceptions;

public class InvalidEmailException extends RuntimeException{

    public InvalidEmailException(final String message) {
        super(message);
    }

}
