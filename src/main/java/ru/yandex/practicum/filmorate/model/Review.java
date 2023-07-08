package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;

@Data
@Valid
@Builder
//@AllArgsConstructor
public class Review {
    private int reviewId; // id отзыва (первичный ключ)
    @NonNull
    private String content; // текст отзыва о фильме
    @NonNull
    public Boolean isPositive; // характеристика отзыва true- положительный, false - отрицательный
    private int userId; // id пользователя который оставил отзыв

    private int filmId; // id фильма на который оставлен отзыв

    private int useful; // рейтинг полезности

//    public Review(int reviewId,  String content,  Boolean isPositive,  int filmId,int userId) {
//        this.reviewId = reviewId;
//        this.content = content;
//        this.isPositive = isPositive;
//        this.userId = userId;
//        this.filmId = filmId;
//    }

    public boolean isPositive() {
        return this.isPositive;
    }
}
