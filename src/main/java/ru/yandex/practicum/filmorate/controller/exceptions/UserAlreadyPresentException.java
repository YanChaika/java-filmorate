package ru.yandex.practicum.filmorate.controller.exceptions;

public class UserAlreadyPresentException extends RuntimeException{

    public UserAlreadyPresentException(final String message) {
        super(message);
    }

}

