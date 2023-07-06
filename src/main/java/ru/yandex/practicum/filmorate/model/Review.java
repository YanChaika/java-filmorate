package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Review {
    private int reviewId; // id отзыва (первичный ключ)
    @NonNull
    private String content; // текст отзыва о фильме
    @NonNull
    public Boolean isPositive; // характеристика отзыва true- положительный, false - отрицательный
    private int userId; // id пользователя который оставил отзыв
    private int filmId; // id фильма на который оставлен отзыв

    private int useful; // рейтинг полезности

    public Review(int reviewId, @NotNull String content,  @NonNull Boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public Review(@NonNull String content, @NotNull Boolean isPositive, int filmId, int useful) {
        this.content = content;
        this.isPositive = isPositive;
        this.filmId = filmId;
        this.useful = useful;
    }

    public Review() {
    }

    public boolean isPositive() {
        return this.isPositive;
    }
}
