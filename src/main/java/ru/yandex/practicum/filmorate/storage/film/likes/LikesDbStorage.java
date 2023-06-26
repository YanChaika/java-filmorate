package ru.yandex.practicum.filmorate.storage.film.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class LikesDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Like create(LikeToCreate like) {
        String sqlQuery = "INSERT INTO PUBLIC.FILM_LIKES (film_id, user_id) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, like.getFilmId());
                    stmt.setInt(2, like.getUserId());
                    return stmt;
                },
                keyHolder);

        return like.asCreated(like.getFilmId(), like.getUserId());
    }

    public void remove(LikeToCreate like) {
        String sqlQuery = "DELETE FROM PUBLIC.FILM_LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                like.getFilmId(),
                like.getUserId()
        );
    }

    public Like update(LikeToCreate like) {
        String sqlQuery = "UPDATE PUBLIC.FILM_LIKES SET " +
                "film_ID = ?, " +
                "user_ID = ? " +
                " WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                like.getFilmId(),
                like.getUserId()
        );
        return like.asCreated(like.getFilmId(), like.getUserId());
    }

    public List<Like> getAll() {
        return jdbcTemplate.query("SELECT FILM_ID, USER_ID FROM PUBLIC.FILM_LIKES ", LikesDbStorage::cons);
    }

    public Set<Like> getSortedFilms() {
        jdbcTemplate.query("SELECT DISTINCT (film_id) AS f, COUNT(user_id) AS c FROM PUBLIC.FILM_LIKES GROUP BY film_id", LikesDbStorage::cons);
        Set<Like> filmsByLikes;
        List<Like> filmsLike = jdbcTemplate.query(
                "SELECT DISTINCT film_id AS f, COUNT(user_id) AS c FROM PUBLIC.FILM_LIKES GROUP BY film_id ORDER BY COUNT(user_id) DESC",
                LikesDbStorage::cons
        );
        if (!filmsLike.isEmpty()) {
            filmsByLikes = new TreeSet<>(new Comparator<Like>() {
                @Override
                public int compare(Like o1, Like o2) {
                    return Integer.compare(o2.getFilmId(), o1.getFilmId());
                }
            });
            for (Like like : filmsLike) {
                filmsByLikes.add(like);
            }
            return filmsByLikes;
        }
        return new HashSet<>();
    }

    private static Like cons(ResultSet rs, int rowNum) throws SQLException {
        return new Like(
                rs.getInt("f"),
                rs.getInt("c")
        );
    }
}
