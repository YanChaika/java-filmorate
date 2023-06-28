package ru.yandex.practicum.filmorate.storage.film.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmMPA> getAllMpa() {
        return jdbcTemplate.query("SELECT RATING_ID, RATING_NAME FROM PUBLIC.FILM_RATING", MpaDbStorage::cons);
    }

    private static FilmMPA cons(ResultSet rs, int rowNum) throws SQLException {
        return new FilmMPA(
                rs.getInt("rating_id"),
                rs.getString("rating_name")
        );
    }

    @Override
    public FilmMPA getMpaById(int id) {
        final String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM PUBLIC.FILM_RATING WHERE rating_id = ?";
        final List<FilmMPA> rating = jdbcTemplate.query(sqlQuery, MpaDbStorage::cons, id);

        if (rating.isEmpty()) {
            throw new IncorrectIdException("Mpa not found");
        } else if (rating.size() > 1) {
            throw new IllegalStateException();
        } else {
            return rating.get(0);
        }
    }
}

