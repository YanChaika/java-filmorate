package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.director.FilmsDirectorsRelationStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final LikesDbStorage likesDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenresDbStorage genresDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmsDirectorsRelationStorage filmsDirectorsRelationStorage;
    private final DirectorStorage directorStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO PUBLIC.films (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        final Film finalFilm = film;

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                    stmt.setString(1, finalFilm.getName());
                    stmt.setString(2, finalFilm.getDescription());
                    stmt.setDate(3, Date.valueOf(finalFilm.getReleaseDate()));
                    stmt.setInt(4, finalFilm.getDuration());
                    stmt.setInt(5, finalFilm.getMpa().getId());
                    return stmt;
                },
                keyHolder);

        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film = film.asCreated(filmId);

        if (film.getDirectors() != null) {
            saveFilmDirectors(film);
        }

        return film;
    }

    private void saveFilmDirectors(Film film) {
        String sqlQuery = "INSERT INTO PUBLIC.films_director (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
        film.getDirectors()
                .forEach(director ->
                        jdbcTemplate.update(connection -> {
                            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                            stmt.setLong(1, film.getId());
                            stmt.setInt(2, director.getId());
                            return stmt;
                        }));
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());
        String sqlQuery = "UPDATE PUBLIC.films SET " +
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
        filmsDirectorsRelationStorage.deleteByFilmId(film.getId());
        if (film.getDirectors() != null) {
            saveFilmDirectors(film);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query("SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "RATING_ID FROM PUBLIC.films", FilmDbStorage::cons);
        for (Film film : films) {
            List<Genre> genres = new ArrayList<>();
            if (film.getGenres() != null) {
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
                    log.info("");
                }
            }
            film.setDirectors(getDirectors(film));
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
    public void delete(Integer id) {
        String sqlQuery = "DELETE FROM PUBLIC.films where film_id=?";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film getFilmById(Integer id) {
        final String sqlQuery = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID " +
                "FROM PUBLIC.films WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::cons, id);
        for (Film film : films) {
            List<Genre> genres = new ArrayList<>();
            if (film.getGenres() != null) {
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(
                            genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get().getName()
                    );
                    genres.add(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
            }
            try {
                List<FilmByGenres> idGenresByFilm = genresDbStorage.getGenreById(film.getId());
                for (FilmByGenres filmByGenres : idGenresByFilm) {
                    genres.add(genreDbStorage.getGenreById(filmByGenres.getGenreId()).get());
                }
            } catch (IncorrectIdException e) {
                log.info("");
            }
            film.setGenres(genres);

            film.setDirectors(getDirectors(film));

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

    @NotNull
    private Set<Director> getDirectors(Film film) {
        Set<Director> directors = new HashSet<>();
        List<FilmDirectorRelation> dfRelations = filmsDirectorsRelationStorage.getByFilmId(film.getId());
        for (FilmDirectorRelation dfRelation : dfRelations) {
            directors.add(directorStorage.getDirector(dfRelation.getDirectorId()));
        }
        return directors;
    }

    @Override
    public List<Film> getSortedFilms() {
        Set<Integer> sortedFilmsIdByLike = likesDbStorage.getSortedFilms();
        List<Film> countBySortedFilms = new ArrayList<>();
        if (!sortedFilmsIdByLike.isEmpty()) {
            for (Integer integer : sortedFilmsIdByLike) {
                countBySortedFilms.add(getFilmById(integer));
            }
        }
        return countBySortedFilms;
    }

    @Override
    public List<Film> getFilmsByDirectorSortedByYear(int directorId) {
        Set<Integer> filmIdsByDirector = getFilmIdsByDirector(directorId);
        List<Film> filmsByIds = getFilmsByIds(filmIdsByDirector);
        filmsByIds.sort(Comparator.comparing(Film::getReleaseDate));
        return filmsByIds;
    }

    @Override
    public List<Film> getFilmsByDirectorSortLikes(int directorId) {
        Set<Integer> filmIdsByDirector = getFilmIdsByDirector(directorId);
        List<Integer> sortedFilmIdsByLikes = likesDbStorage.getSortedFilmsByIds(filmIdsByDirector);
        return getFilmsByIds(sortedFilmIdsByLikes);
    }

    @Override
    public List<Film> filmsSearchInDirector(String searchQuery) {
        final String sqlQuery = "SELECT DISTINCT fd.FILM_ID FROM PUBLIC.films_director AS fd " +
                "LEFT OUTER JOIN PUBLIC.directors AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                "WHERE LOWER(d.DIRECTOR_NAME) LIKE '%" + searchQuery + "%' ORDER BY fd.FILM_ID";
        final List<Integer> filmsId = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        return filmsId.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    @Override
    public List<Film> filmsSearchInTitle(String searchQuery) {
        final String sqlQuery = "SELECT DISTINCT FILM_ID FROM PUBLIC.films " +
                "WHERE LOWER(FILM_NAME) LIKE '%" + searchQuery + "%'";
        final List<Integer> filmsId = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        return filmsId.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    @NotNull
    private List<Film> getFilmsByIds(Collection<Integer> sortedFilmIdsByLikes) {
        List<Film> films = new ArrayList<>();
        for (Integer filmId : sortedFilmIdsByLikes) {
            films.add(getFilmById(filmId));
        }
        return films;
    }

    @NotNull
    private Set<Integer> getFilmIdsByDirector(int directorId) {
        List<FilmDirectorRelation> filmIdsByDirector = filmsDirectorsRelationStorage.getByDirectorId(directorId);
        if (filmIdsByDirector.isEmpty()) {
            throw new IncorrectIdException("Films not found");
        }
        return filmIdsByDirector.stream()
                .map(FilmDirectorRelation::getFilmId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Film> getTwoUsersCommonFilms(Integer userId, Integer friendId) {
        Set<Integer> commonFilmsId = likesDbStorage.getCommonFilmsId(userId, friendId);
        List<Film> commonFilms = new ArrayList<>();
        if (!commonFilmsId.isEmpty()) {
            for (Integer integer : commonFilmsId) {
                commonFilms.add(getFilmById(integer));
            }
        }
        return commonFilms;
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