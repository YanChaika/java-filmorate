package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer id, Integer userId) {
        Film filmToUpdate = filmStorage.getFilmById(id);
        if (filmToUpdate == null) {
            throw new IncorrectIdException("Film для добавления лайка не найден");
        }
        Set<Integer> likesByFilm = filmToUpdate.getLikes();
        if (likesByFilm == null) {
            likesByFilm = new HashSet<>();
        }
        likesByFilm.add(userId);
        filmToUpdate.setLikes(likesByFilm);
        filmToUpdate.setCountLikes(filmToUpdate.getLikes().size());
        return filmStorage.update(filmToUpdate);
    }

    public Film removeLike(Integer id, Integer userId) {
        try {
            Film filmToUpdate = filmStorage.getFilmById(id);
            Set<Integer> likesByFilm = filmToUpdate.getLikes();
            likesByFilm.remove(userId);
            return filmStorage.update(filmToUpdate);
        } catch (NullPointerException e) {
            throw new IncorrectIdException("Film для удаления не найден");
        }
    }

    public List<Film> getCountFilmsByLike(Integer count) {
        List<Film> sortedFilms = new ArrayList<>(filmStorage.getSortedFilms());
        List<Film> countBySortedFilms = new ArrayList<>();
        if (!sortedFilms.isEmpty()) {
            int minOfCountAndSizeOfSortedFilms;
            if (sortedFilms.size() < count) {
                minOfCountAndSizeOfSortedFilms = sortedFilms.size();
            } else {
                minOfCountAndSizeOfSortedFilms = count;
            }
            for (int i = 0; i < minOfCountAndSizeOfSortedFilms; i++)
                countBySortedFilms.add(sortedFilms.get(i));
        }
        return countBySortedFilms;
    }
}
