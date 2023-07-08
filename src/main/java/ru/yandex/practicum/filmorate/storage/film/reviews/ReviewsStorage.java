package ru.yandex.practicum.filmorate.storage.film.reviews;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewsStorage {
    Review addReview(Review review);

    Review updateReview(Review review);

    Review getReviewById(int id);

    void deleteReviewById(int id);

    void addLikeReview(int reviewId, int userId);

    void addDisLikeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);

    List<Review> getReviews(int filmId, int quantity);
}
