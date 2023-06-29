package ru.yandex.practicum.filmorate.storage.film.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT GENRE_ID, GENRE_NAME FROM PUBLIC.FILM_GENRE", GenreDbStorage::cons);
    }

    private static Genre cons(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        );
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM PUBLIC.FILM_GENRE WHERE GENRE_ID = ?";
        final List<Genre> genre = jdbcTemplate.query(sqlQuery, GenreDbStorage::cons, id);

        if (genre.isEmpty()) {
            throw new IncorrectIdException("Genre not found");
        } else if (genre.size() > 1) {
            throw new IllegalStateException();
        } else {
            return Optional.of(genre.get(0));
        }
    }
}
