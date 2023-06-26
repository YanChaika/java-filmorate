package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmByGenres;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.FilmMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikeToCreate;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesDbStorage;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmService {

    @Autowired
    private final FilmStorage filmStorage;
    private final LikesDbStorage likesDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    public FilmService(FilmStorage filmStorage,
                       LikesDbStorage likesDbStorage,
                       GenreDbStorage genreDbStorage,
                       MpaDbStorage mpaDbStorage,
                       GenresDbStorage genresDbStorage
    ) {
        this.filmStorage = filmStorage;
        this.likesDbStorage = likesDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genresDbStorage = genresDbStorage;
    }

    public void addLike(Integer id, Integer userId) {
        Film filmToUpdate = filmStorage.getFilmById(id);
        if (filmToUpdate == null) {
            throw new IncorrectIdException("Film для добавления лайка не найден");
        }
        likesDbStorage.create(new LikeToCreate(id, userId));
    }

    public void removeLike(Integer id, Integer userId) {
        try {
            Film filmToUpdate = filmStorage.getFilmById(id);
            likesDbStorage.remove(new LikeToCreate(id, userId));
        } catch (NullPointerException e) {
            throw new IncorrectIdException("Film для удаления не найден");
        }
    }

    public List<Film> getCountFilmsByLike(Integer count) {
        List<Film> filmsSorted = filmStorage.getSortedFilms();
        List<Film> countOfSortedFilm = new ArrayList<>();
        List<Film> notCountOfSorted = new ArrayList<>(filmsSorted);
        if (!filmsSorted.isEmpty()) {
            int minCountOrSizeOfSortedFilms;
            if (filmsSorted.size() < count) {
                 minCountOrSizeOfSortedFilms = filmsSorted.size();
            } else {
                minCountOrSizeOfSortedFilms = count;
            }
            for (int i = 0; i < minCountOrSizeOfSortedFilms; i++) {
                countOfSortedFilm.add(notCountOfSorted.get(i));
            }
        }
        return countOfSortedFilm;
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
            film.getMpa().setName(mpaDbStorage.getMpaById(film.getMpa().getId()).getName());
            Film filmToReturn = filmStorage.create(film);
            if (film.getRate() != 0) {
                likesDbStorage.create(new LikeToCreate(filmToReturn.getId(), 0));
            }
            List<Genre> genres = new ArrayList<>();
            if (film.getGenres() != null) {
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get().getName());
                    genresDbStorage.create(new FilmByGenres(filmToReturn.getId(), film.getGenres().get(i).getId()));
                    genres.add(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get());
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
            if (film.getRate() != 0) {
                likesDbStorage.remove(new LikeToCreate(film.getId(), 0));
                likesDbStorage.create(new LikeToCreate(film.getId(), 0));
            }
            List<Genre> genres = new ArrayList<>();
            Set<Genre> unicGenres = new HashSet<>();
            if (film.getGenres() != null) {
                genresDbStorage.remove(film.getId());
                for (int i = 0; i < film.getGenres().size(); i++) {
                    film.getGenres().get(i).setName(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get().getName());
                    unicGenres.add(genreDbStorage.getGenreById(film.getGenres().get(i).getId()).get());
                }
                for (Genre unicGenre : unicGenres) {
                    genresDbStorage.create(new FilmByGenres(film.getId(), unicGenre.getId()));
                    genres.add(unicGenre);
                }
            } else {
                genresDbStorage.remove(film.getId());
            }
            film.setGenres(genres);
            film.getMpa().setName(mpaDbStorage.getMpaById(film.getMpa().getId()).getName());
            return filmStorage.update(film);
        }
    }

    public Film getFilmById(Integer id) {
        Film filmToAddGenre = filmStorage.getFilmById(id);
        return filmToAddGenre;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAll();
    }

    public Optional<Genre> getGenreById(int id) {
        return genreDbStorage.getGenreById(id);
    }

    public List<FilmMPA> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    public Optional<FilmMPA> getMpaById(int id) {
        return Optional.of(mpaDbStorage.getMpaById(id));
    }
}
