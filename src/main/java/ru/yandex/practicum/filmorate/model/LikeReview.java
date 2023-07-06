package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

//  лайк/дизлайк отзыва
@Data
@AllArgsConstructor
public class LikeReview {
    private int userId; // id пользователя который поставил лайк отзывву
    private int reviewId; // id отзыва которому поставили лайк
    private boolean isLike; // если true то это лайк, если false то это дизлайк
}
