package ru.yandex.practicum.filmorate.storage.film.reviews;

public interface LikeReviewStorage {
    void addLikeReview(int reviewId, int userId);

    void addDisLikeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);
}
