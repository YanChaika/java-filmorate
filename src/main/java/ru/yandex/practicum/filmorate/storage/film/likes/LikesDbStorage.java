package ru.yandex.practicum.filmorate.storage.film.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.*;

@Component
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(int filmId, int userId) {
        String sqlQuery = "INSERT INTO PUBLIC.FILM_LIKES (film_id, user_id) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, filmId);
                    stmt.setInt(2, userId);
                    return stmt;
                },
                keyHolder);
    }

    @Override
    public void remove(int filmId, int userId) {
        String sqlQuery = "DELETE FROM PUBLIC.FILM_LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                filmId,
                userId
        );
    }

    @Override
    public void update(int filmId, int userId) {
        String sqlQuery = "UPDATE PUBLIC.FILM_LIKES SET " +
                "film_ID = ?, " +
                "user_ID = ? " +
                " WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                filmId,
                userId
        );
    }

    @Override
    public Set<Integer> getSortedFilms() {
        Set<Integer> filmsId = new HashSet<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT DISTINCT (film_id), COUNT(user_id) " +
                "FROM PUBLIC.FILM_LIKES " +
                "GROUP BY film_id ORDER BY COUNT(user_id) DESC");
        while (userRows.next()) {
            filmsId.add(userRows.getInt("film_id"));
        }
        return filmsId;
    }
}
