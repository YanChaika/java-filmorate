package ru.yandex.practicum.filmorate.storage.film.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmDirectorRelation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmsDirectorsDbRelationStorage implements FilmsDirectorsRelationStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmsDirectorsDbRelationStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmDirectorRelation> getByFilmId(int id) {
        final String sqlQuery = "SELECT FILM_ID, DIRECTOR_ID FROM PUBLIC.FILMS_DIRECTOR WHERE FILM_ID = ?";
        final List<FilmDirectorRelation> relations = jdbcTemplate.query(sqlQuery, FilmsDirectorsDbRelationStorage::cons, id);
        return relations;
    }

    @Override
    public void deleteByFilmId(int id) {
        String sqlQuery = "DELETE FROM PUBLIC.FILMS_DIRECTOR WHERE film_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                id
        );
    }

    @Override
    public List<FilmDirectorRelation> getByDirectorId(int id) {
        final String sqlQuery = "SELECT FILM_ID, DIRECTOR_ID FROM PUBLIC.FILMS_DIRECTOR WHERE DIRECTOR_ID = ?";
        final List<FilmDirectorRelation> relations = jdbcTemplate.query(sqlQuery, FilmsDirectorsDbRelationStorage::cons, id);
        return relations;
    }

    private static FilmDirectorRelation cons(ResultSet rs, int rowNum) throws SQLException {
        return new FilmDirectorRelation(
                rs.getInt("film_id"),
                rs.getInt("director_id")
        );
    }
}