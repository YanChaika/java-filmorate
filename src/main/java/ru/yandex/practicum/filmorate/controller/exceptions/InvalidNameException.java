package ru.yandex.practicum.filmorate.controller.exceptions;

public class InvalidNameException extends RuntimeException{

    public InvalidNameException(final String message) {
        super(message);
    }

}
