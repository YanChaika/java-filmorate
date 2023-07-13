package ru.yandex.practicum.filmorate.storage.film.reviews;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
@Data
@Slf4j
public class LikeReviewDbStorage implements LikeReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    //пользователь ставит лайк отзыву
    @Override
    public void addLikeReview(int reviewId, int userId) {
        String sqlQuery = "INSERT INTO PUBLIC.likes_review(USER_ID, REVIEW_ID,IS_LIKE) values (?, ?, ?)";
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
        String sqlQuery = "INSERT INTO PUBLIC.likes_review (USER_ID, REVIEW_ID,IS_LIKE) values (?, ?, ?)";
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
        String sqlQuery = "delete from PUBLIC.likes_review  where REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }
}
