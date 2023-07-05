package ru.yandex.practicum.filmorate.storage.film.reviews;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Data
@Slf4j
public class ReviewsDbStorage implements ReviewsStorage {
    private final JdbcTemplate jdbcTemplate;

    //Добавление нового отзыва
    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO PUBLIC.REVIEWS (content, IS_POSITIVE,user_Id,film_Id) values (?, ?, ?, ?)";
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
        return review;
    }

    //Редактирование уже имеющегося отзыва
    public Review updateReview(Review review) {
        String sqlQuery = "UPDATE PUBLIC.REVIEWS SET " +
                "content = ?, " +
                "IS_POSITIVE = ?, " +
                "user_ID = ?, " +
                "film_ID = ?" +
                " WHERE REVIEW_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                review.getContent(),
                review.isPositive(),
                review.getFilmId(),
                review.getFilmId(),
                review.getReviewId()
        );
        return review;
    }

    //Получение отзыва по идентификатору
    public Review getReviewById(int id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("select * from PUBLIC.REVIEWS where REVIEW_ID = ?", id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("REVIEW_ID"),
                    reviewRows.getString("content"),
                    reviewRows.getBoolean("IS_POSITIVE"),
                    reviewRows.getInt("user_ID"),
                    reviewRows.getInt("film_ID")
            );
            return review;
        } else {
            log.error("Review by id not found");
            throw new IncorrectIdException("Review not found");
        }
    }

    //Удаление уже имеющегося отзыва
    public void deleteReviewById(int id) {
        Review review = getReviewById(id);
        String sqlQuery = "delete from PUBLIC.REVIEWS where REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, review.getReviewId());
    }

    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.
    public List<Review> getReviews(int filmId) {
        String sqlQuery = "SELECT * FROM PUBLIC.REVIEWS where film_id = ?";
        return jdbcTemplate.query(sqlQuery + filmId, (rs, rowNum) -> {
            Review review = new Review(
                    rs.getInt("REVIEW_ID"),
                    rs.getString("content"),
                    rs.getBoolean("IS_POSITIVE"),
                    rs.getInt("user_ID"),
                    rs.getInt("film_ID")
            );
            return review;
        });
    }
}
