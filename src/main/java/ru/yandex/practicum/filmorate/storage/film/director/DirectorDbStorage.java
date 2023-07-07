package ru.yandex.practicum.filmorate.storage.film.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director createDirector(Director director) {
        if (director.getName() == null || director.getName().isEmpty()) {
            throw new ValidationException("Director name must not be empty.");
        }

        String sqlQuery = "INSERT INTO PUBLIC.directors (DIRECTOR_NAME) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"DIRECTOR_ID"});
                        preparedStatement.setString(1, director.getName());
                        return preparedStatement;
                    },
                    keyHolder
            );
            director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        } catch (DataAccessException e) {
            throw new ValidationException("Failed to create director. Error: " + e.getMessage());
        }

        return director;
    }


    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE PUBLIC.directors SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";

        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return getDirector(director.getId());
    }


    @Override
    public Boolean deleteDirector(int directorId) {
        String sqlQuery = "DELETE FROM PUBLIC.directors " +
                "WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery, directorId);
        return true;
    }

    @Override
    public Director getDirector(int directorId) {
        String sqlQuery = "SELECT DIRECTOR_ID, DIRECTOR_NAME FROM PUBLIC.directors WHERE DIRECTOR_ID = ?";
        Director director;
        List<Director> directors = jdbcTemplate.query(sqlQuery, DirectorDbStorage::mapRow, directorId);
        if (directors.isEmpty()) {
            throw new IncorrectIdException("Режиссера с таким id не существует.");
        } else {
            director = directors.get(0);
        }
        return director;
    }

    @Override
    public List<Director> getAllDirectors() {
        return jdbcTemplate.query("SELECT * FROM PUBLIC.directors", DirectorDbStorage::mapRow);
    }

    static Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("DIRECTOR_ID"))
                .name(rs.getString("DIRECTOR_NAME"))
                .build();
    }
}
