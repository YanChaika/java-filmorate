package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmByGenres;
import ru.yandex.practicum.filmorate.model.FilmMPA;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenresStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final GenresStorage genresStorage;
    private final MpaStorage mpaStorage;
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    public void addLike(Integer id, Integer userId) {
        Film filmToUpdate = filmStorage.getFilmById(id);
        if (filmToUpdate == null) {
            throw new IncorrectIdException("Film для добавления лайка не найден");
        }
        likesStorage.create(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        try {
            Film filmToUpdate = filmStorage.getFilmById(id);
            likesStorage.remove(id, userId);
        } catch (NullPointerException e) {
            throw new IncorrectIdException("Film для удаления не найден");
        }
    }

    public void removeFilm(Integer id) {
        filmStorage.delete(id);
    }

    public List<Film> getCountFilmsByLike(Integer count) {
        List<Film> filmsSorted = filmStorage.getSortedFilms();
        List<Film> countOfSortedFilm = new ArrayList<>();
        if (!filmsSorted.isEmpty()) {
            int minCountOrSizeOfSortedFilms;
            if (filmsSorted.size() < count) {
                minCountOrSizeOfSortedFilms = filmsSorted.size();
            } else {
                minCountOrSizeOfSortedFilms = count;
            }
            for (int i = 0; i < minCountOrSizeOfSortedFilms; i++) {
                countOfSortedFilm.add(filmsSorted.get(i));
            }
        } else {
            // countOfSortedFilm = filmStorage.getAll(); // если таблица likes пустая, то filmStorage.getSortedFilms()
            // возвращает пустой List, и метод в итоге возвращает список вообще всех фильмов.
            // предлагаю сделать хотя бы так:
            countOfSortedFilm = filmStorage.getAll().stream().limit(count).collect(Collectors.toList());
        }
        return countOfSortedFilm;
    }

    public List<Film> getTwoUsersCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getTwoUsersCommonFilms(userId, friendId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be post film");
        } else {
            film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).getName());
            Film filmToReturn = filmStorage.create(film);
            List<Genre> genres = new ArrayList<>();
            if (film.getGenres() != null) {
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(genreStorage.getGenreById(film.getGenres().get(i).getId()).get().getName());
                    genresStorage.create(new FilmByGenres(filmToReturn.getId(), film.getGenres().get(i).getId()));
                    genres.add(genreStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
            }
            filmToReturn.setGenres(genres);
            return filmToReturn;
        }
    }

    public Film update(Film film) {
        if ((film.getName().isBlank()) ||
                (film.getDescription().getBytes(StandardCharsets.UTF_8).length > 200) ||
                (film.getReleaseDate().isBefore(earliestReleaseDate)) ||
                (film.getDuration() <= 0)
        ) {
            throw new ValidationException("Error: can't be put film");
        } else {
            filmStorage.getFilmById(film.getId());
            List<Genre> genres = new ArrayList<>();
            Set<Genre> unicGenres = new HashSet<>();
            if (film.getGenres() != null) {
                genresStorage.remove(film.getId());
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(genreStorage.getGenreById(film.getGenres().get(i).getId()).get().getName());
                    unicGenres.add(genreStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
                for (Genre unicGenre : unicGenres) {
                    genresStorage.create(new FilmByGenres(film.getId(), unicGenre.getId()));
                    genres.add(unicGenre);
                }
            } else {
                genresStorage.remove(film.getId());
            }
            film.setGenres(genres);
            film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).getName());
            return filmStorage.update(film);
        }
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Optional<Genre> getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<FilmMPA> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Optional<FilmMPA> getMpaById(int id) {
        return Optional.of(mpaStorage.getMpaById(id));
    }

    public List<Film> getByDirectorId(Integer directorId, String sortBy) {
        if ("year".equals(sortBy)) {
            return filmStorage.getFilmsByDirectorSortedByYear(directorId);
        }
        return filmStorage.getFilmsByDirectorSortLikes(directorId);
    }
}