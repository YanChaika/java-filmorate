package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;

@Data
@Valid
@Builder
public class Review {
    private int reviewId; // id отзыва (первичный ключ)
    @NonNull
    private String content; // текст отзыва о фильме
    @NonNull
    public Boolean isPositive; // характеристика отзыва true- положительный, false - отрицательный
    private int userId; // id пользователя который оставил отзыв

    private int filmId; // id фильма на который оставлен отзыв

    private int useful; // рейтинг полезности

    public boolean isPositive() {
        return this.isPositive;
    }
}
