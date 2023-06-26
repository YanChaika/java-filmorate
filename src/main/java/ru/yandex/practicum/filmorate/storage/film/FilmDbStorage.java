package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesDbStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final LikesDbStorage likesDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenresDbStorage genresDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(
            JdbcTemplate jdbcTemplate,
                         LikesDbStorage likesDbStorage,
                         MpaDbStorage mpaDbStorage,
                         GenresDbStorage genresDbStorage,
                         GenreDbStorage genreDbStorage
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.likesDbStorage = likesDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genresDbStorage = genresDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO PUBLIC.FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                    stmt.setString(1, film.getName());
                    stmt.setString(2, film.getDescription());
                    stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                    stmt.setInt(4, film.getDuration());
                    stmt.setInt(5, film.getMpa().getId());
                    return stmt;
                },
                keyHolder);

        return film.asCreated(
                Objects.requireNonNull(keyHolder.getKey()).intValue()
        );
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());
            String sqlQuery = "UPDATE PUBLIC.FILMS SET " +
                    "film_name = ?, " +
                    "description = ?, " +
                    "release_date = ?, " +
                    "duration = ?, " +
                    "rating_id = ? WHERE film_id = ?";
            jdbcTemplate.update(
                    sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query("SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID FROM PUBLIC.FILMS", FilmDbStorage::cons);
        for (Film film : films) {
            List<Genre> genres = new ArrayList<>();
            if(film.getGenres() != null) {
                for (int i = 0; i < film.getGenres().size(); i++) {
                    genres.add(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
            } else {
                try {
                    List<FilmByGenres> idGenresByFilm = genresDbStorage.getGenreById(film.getId());
                    for (FilmByGenres filmByGenres : idGenresByFilm) {
                        genres.add(genreDbStorage.getGenreById(filmByGenres.getGenreId()).get());
                    }
                } catch (IncorrectIdException e) {

                }
            }
            film.setGenres(genres);
            film.getMpa().setName(mpaDbStorage.getMpaById(film.getMpa().getId()).getName());
        }
        if (films.isEmpty()) {
            return new ArrayList<>();
        } else {
            return films;
        }
    }

    @Override
    public Film delete(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID FROM PUBLIC.FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::cons, id);
        for (Film film : films) {
            List<Genre> genres = new ArrayList<>();
            if(film.getGenres() != null) {
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get().getName());
                    genres.add(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
            }
            try {
                List<FilmByGenres> idGenresByFilm = genresDbStorage.getGenreById(film.getId());
                for (FilmByGenres filmByGenres : idGenresByFilm) {
                    genres.add(genreDbStorage.getGenreById(filmByGenres.getGenreId()).get());
                }
            } catch (IncorrectIdException e) {

            }
            film.setGenres(genres);

            film.getMpa().setName(mpaDbStorage.getMpaById(film.getMpa().getId()).getName());
        }
        if (films.isEmpty()) {
            throw new IncorrectIdException("Film with id " + id + " not found");
        } else if (films.size() > 1) {
            throw new IllegalStateException();
        } else {
            return films.get(0);
        }
    }

    @Override
    public List<Film> getSortedFilms() {
        Set<Like> sortedFilmsIdByLike = likesDbStorage.getSortedFilms();
        List<Film> countBySortedFilms = new ArrayList<>();
        for (Like like : sortedFilmsIdByLike) {
            countBySortedFilms.add(getFilmById(like.getFilmId()));
        }
        return countBySortedFilms;
    }


    private static Film cons(ResultSet rs, int rowNum) throws SQLException {
        FilmMPA filmMPA = new FilmMPA(rs.getInt("rating_id"), null);
        return new Film(
                rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                filmMPA
        );
    }
}
