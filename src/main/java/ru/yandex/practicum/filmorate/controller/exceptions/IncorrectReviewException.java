package ru.yandex.practicum.filmorate.controller.exceptions;

public class IncorrectReviewException extends RuntimeException {
    public IncorrectReviewException(String message) {
        super(message);
    }
}
