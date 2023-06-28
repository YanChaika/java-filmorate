package ru.yandex.practicum.filmorate.storage.film.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmByGenres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenresDbStorage implements GenresStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FilmByGenres create(FilmByGenres filmByGenres) {
        String sqlQuery = "INSERT INTO PUBLIC.FILM_BY_GENRES (film_id, genre_id) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, filmByGenres.getFilmId());
                    stmt.setInt(2, filmByGenres.getGenreId());
                    return stmt;
                },
                keyHolder);

        return filmByGenres;
    }

    @Override
    public void remove(int id) {
        String sqlQuery = "DELETE FROM PUBLIC.FILM_BY_GENRES WHERE film_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                id
        );
    }

    @Override
    public List<FilmByGenres> getAll() {
        return jdbcTemplate.query("SELECT FILM_ID, GENRE_ID FROM PUBLIC.FILM_BY_GENRES", GenresDbStorage::cons);
    }

    private static FilmByGenres cons(ResultSet rs, int rowNum) throws SQLException {
        return new FilmByGenres(
                rs.getInt("film_id"),
                rs.getInt("genre_id")
        );
    }

    @Override
    public List<FilmByGenres> getGenreById(int id) {
        final String sqlQuery = "SELECT FILM_ID, GENRE_ID FROM PUBLIC.FILM_BY_GENRES WHERE FILM_ID = ?";
        final List<FilmByGenres> genres = jdbcTemplate.query(sqlQuery, GenresDbStorage::cons, id);
        if (genres.isEmpty()) {
            throw new IncorrectIdException("Genres not found");
        } else {
            return genres;
        }
    }
}
