package ru.yandex.practicum.filmorate.storage.film.reviews;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewsStorage {
   Review addReview (Review review);
   Review updateReview(Review review);
   Review getReviewById(int id);
   void deleteReviewById (int id);
   List<Review> getReviews(int filmId);
}
