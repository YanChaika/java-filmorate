package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Array;

@Data
@AllArgsConstructor
public class Review {
    private int reviewId; // id отзыва (первичный ключ)
    private String content; // текст отзыва о фильме
    private boolean isPositive; // характеристика отзыва true- положительный, false - отрицательный
    private int userId; // id пользователя который оставил отзыв
    private int filmId; // id фильма на который оставлен отзыв
    @JsonIgnore
    private int[] usersIdLike; // массив id пользователей поставивших лайк отзыву
    @JsonIgnore
    private int[] usersIdDislike; // массив id пользователей поставивших дизлайк отзыву
    @JsonIgnore
    private int like; // количество лайков (рейтинг полезности )

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public Review() {
    }

    public int getLike() {
        return like=usersIdLike.length-usersIdDislike.length;
    }
}