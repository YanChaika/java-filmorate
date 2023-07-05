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
    public  Review getReviewById(@PathVariable int id) {
        return filmService.getReviewById(id);
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReviewById(@PathVariable int id) {
        filmService.deleteReviewById(id);
    }

    @GetMapping("/reviews?filmId={filmId}")
   public List<Review> getReviews(@PathVariable int filmId) {
        return filmService.getReviews(filmId);
    }
}
