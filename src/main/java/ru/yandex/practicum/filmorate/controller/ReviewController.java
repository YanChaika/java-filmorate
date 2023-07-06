package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.reviews.ReviewsStorage;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final FilmService filmService;
    private final ReviewsStorage reviewsStorage;

    // тз 12 групповой проект
    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review) {
        return filmService.addReview(review);
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review review) {
        return filmService.updateReview(review);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable int id) {
        return filmService.getReviewById(id);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReviewById(@PathVariable int id) {
        filmService.deleteReviewById(id);
    }

    @GetMapping("/reviews?filmId={filmId}&count={count}")
    public List<Review> getReviews(@PathVariable int filmId, @PathVariable int quantity) {
        return filmService.getReviews(filmId, quantity);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return filmService.getReviews(0, 0);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLikeReview(@PathVariable int id, @PathVariable int userId) {
        reviewsStorage.addLikeReview(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    void addDisLikeReview(@PathVariable int id, @PathVariable int userId) {
        reviewsStorage.addDisLikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    void deleteLikeReview(@PathVariable int id, @PathVariable int userId) {
        reviewsStorage.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    void deleteDisLikeReview(@PathVariable int id, @PathVariable int userId) {
        reviewsStorage.deleteLikeReview(id, userId);
    }
}
