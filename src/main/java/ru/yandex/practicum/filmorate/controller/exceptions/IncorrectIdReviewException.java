package ru.yandex.practicum.filmorate.controller.exceptions;

public class IncorrectIdReviewException extends RuntimeException {
    public IncorrectIdReviewException(String message) {
        super(message);
    }
}
