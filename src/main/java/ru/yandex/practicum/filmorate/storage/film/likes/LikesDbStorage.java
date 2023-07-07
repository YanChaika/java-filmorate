package ru.yandex.practicum.filmorate.storage.film.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
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


    @Override
    public List<Integer> getSortedFilmsByIds(Set<Integer> filmIds) {
        List<Integer> result = new ArrayList<>();
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(String.format("SELECT DISTINCT (film_id), COUNT(user_id) " +
                "FROM PUBLIC.FILM_LIKES " +
                "WHERE film_id in (%s) " +
                "GROUP BY film_id ORDER BY COUNT(user_id) DESC", inSql), filmIds.toArray());
        while (userRows.next()) {
            result.add(userRows.getInt("film_id"));
        }
        for (Integer filmId : filmIds) {
            if (!result.contains(filmId)) {
                result.add(filmId);
            }
        }
        return result;
    }

    @Override
    public List<Integer> getFilmIdByUserId(int id) {
        List<Integer> filmsIdByUser = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT film_id " +
                "FROM PUBLIC.FILM_LIKES " +
                "WHERE user_id = ?", id);
        while (userRows.next()) {
            filmsIdByUser.add(userRows.getInt("film_id"));
        }
        return filmsIdByUser;
    }

    @Override
    public Set<Integer> getCommonFilmsId(Integer userId, Integer friendId) {
        Set<Integer> commonFilmsId = new HashSet<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT DISTINCT C.film_id, COUNT(C.user_id) " +
                "FROM PUBLIC.FILM_LIKES AS C " +
                "INNER JOIN (SELECT film_id " +
                "FROM PUBLIC.FILM_LIKES WHERE user_id = ?) AS U ON C.film_id = U.film_id " +
                "INNER JOIN (SELECT film_id " +
                "FROM PUBLIC.FILM_LIKES WHERE user_id = ?) AS F ON U.film_id = F.film_id " +
                "GROUP BY C.film_id " +
                "ORDER BY COUNT(C.user_id) DESC;", userId, friendId);
        while (userRows.next()) {
            commonFilmsId.add(userRows.getInt("film_id"));
        }
        return commonFilmsId;
    }
}
