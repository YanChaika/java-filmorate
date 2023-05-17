package ru.yandex.practicum.filmorate.controller.exceptions;

public class UserAlreadyException extends RuntimeException{

    public UserAlreadyException(final String message) {
        super(message);
    }

}
