package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final FilmService filmService;

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

    @GetMapping("/reviews")
    public List<Review> getAllReviews(
            @RequestParam(defaultValue = "0") int filmId,
            @RequestParam(defaultValue = "0") int count) {
        return filmService.getReviews(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLikeReview(@PathVariable int id, @PathVariable int userId) {
        filmService.addLikeReview(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void addDisLikeReview(@PathVariable int id, @PathVariable int userId) {
        filmService.addDisLikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void deleteDisLikeReview(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLikeReview(id, userId);
    }
}
