package ru.yandex.practicum.filmorate.storage.film.reviews;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectReviewException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Data
@Slf4j
public class ReviewsDbStorage implements ReviewsStorage {
    private final JdbcTemplate jdbcTemplate;

    //Добавление нового отзыва
    @Override
    public Review addReview(Review review) {
        if (review.getFilmId() == 0 || review.getUserId() == 0) {
            log.error("Incorrect film id or user id");
            throw new IncorrectReviewException("Incorrect film id or user id");
        }
        if (review.getFilmId() < 0 || review.getUserId() < 0) {
            log.error("Incorrect film id or user id");
            throw new IncorrectIdException("Incorrect film id or user id");
        }
        String sqlQuery = "INSERT INTO PUBLIC.REVIEWS (content, IS_POSITIVE,user_Id,film_Id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
                    stmt.setString(1, review.getContent());
                    stmt.setBoolean(2, review.isPositive());
                    stmt.setInt(3, review.getUserId());
                    stmt.setInt(4, review.getFilmId());
                    return stmt;
                },
                keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return getReviewById(id);
    }

    //Редактирование уже имеющегося отзыва
    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "UPDATE PUBLIC.REVIEWS SET " +
                "content = ?, " +
                "IS_POSITIVE = ?" +
                " WHERE REVIEW_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                review.getContent(),
                review.isPositive(),
                review.getReviewId()
        );
        return getReviewById(review.getReviewId());
    }

    //Получение отзыва по идентификатору
    @Override
    public Review getReviewById(int id) {
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet("select * from PUBLIC.REVIEWS where REVIEW_ID = ?", id);
        if (resultSet.next()) {
            Review review = Review.builder()
                    .content(Objects.requireNonNull(resultSet.getString("content")))
                    .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                    .filmId(resultSet.getInt("film_ID"))
                    .userId(resultSet.getInt("user_ID"))
                    .reviewId(resultSet.getInt("REVIEW_ID"))
                    .build();
            int useFul = getUseFull(id);
            review.setUseful(useFul);
            return review;
        } else {
            log.error("Review by id not found");
            throw new IncorrectIdException("Review not found");
        }
    }

    //Удаление уже имеющегося отзыва
    @Override
    public void deleteReviewById(int id) {
        //Review review = getReviewById(id);
        String sqlQuery = "delete from PUBLIC.REVIEWS where REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    //Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10
    @Override
    public List<Review> getReviews(int filmId, int quantity) {
        List<Review> reviews = new ArrayList<>();
        String query;
        if (filmId != 0) {
            query = "SELECT * FROM public.reviews WHERE film_id = " + filmId;
            if (quantity != 0) {
                query += " LIMIT " + quantity;
            } else {
                query += " LIMIT 10";
            }
        } else {
            query = "SELECT * FROM public.reviews";
        }

        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(query);//statement.executeQuery(query);
        while (resultSet.next()) {
            reviews.add(getReviewById(resultSet.getInt("REVIEW_ID")));
        }
        reviews.sort((r1, r2) -> r2.getUseful() - r1.getUseful());
        return reviews;
    }

    //пользователь ставит лайк отзыву
    @Override
    public void addLikeReview(int reviewId, int userId) {
        String sqlQuery = "INSERT INTO PUBLIC.LIKEREVIEW (USER_ID, REVIEW_ID,IS_LIKE) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, userId);
                    stmt.setInt(2, reviewId);
                    stmt.setBoolean(3, true);
                    return stmt;
                },
                keyHolder);
    }

    //пользователь ставит дизлайк отзыву
    @Override
    public void addDisLikeReview(int reviewId, int userId) {
        String sqlQuery = "INSERT INTO PUBLIC.LIKEREVIEW (USER_ID, REVIEW_ID,IS_LIKE) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, userId);
                    stmt.setInt(2, reviewId);
                    stmt.setBoolean(3, false);
                    return stmt;
                },
                keyHolder);
    }

    // пользователь удаляет лайк/дизлайк отзыву
    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        String sqlQuery = "delete from PUBLIC.LIKEREVIEW  where REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    //получение рейтинга полезности отзыва по его id
    public int getUseFull(int reviewId) {
        SqlRowSet sqlForLikes = jdbcTemplate.queryForRowSet("SELECT * from PUBLIC.LIKEREVIEW where REVIEW_ID = ? AND IS_LIKE = true", reviewId);
        int like = 0;
        int disLike = 0;
        while (sqlForLikes.next()) {
            like++; // подсчитываем количество лайков
        }
        SqlRowSet sqlForDisLikes = jdbcTemplate.queryForRowSet("SELECT * from PUBLIC.LIKEREVIEW where REVIEW_ID = ? AND IS_LIKE = false", reviewId);
        while (sqlForDisLikes.next()) {
            disLike++; // подсчитываем количество дизлайков
        }
        return like - disLike;
    }
}
