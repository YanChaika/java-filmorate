package ru.yandex.practicum.filmorate.storage.film.reviews;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
@Data
public class ReviewsDbStorage implements ReviewsStorage {
    private final JdbcTemplate jdbcTemplate;

    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO PUBLIC.REVIEWS (content, isPositive,user_Id,film_Id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setString(1, review.getContent());
                    stmt.setBoolean(2, review.isPositive());
                    stmt.setInt(3, review.getUserId());
                    stmt.setInt(4, review.getFilmId());
                    return stmt;
                },
                keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

}
