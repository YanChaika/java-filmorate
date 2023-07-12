package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final LikesStorage likesStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private final Map<User, List<Film>> diffs = new HashMap<>();
    private final Map<User, List<Film>> userToCompare = new HashMap<>();

    public void getInputData(int id) {
        List<User> users = userStorage.getAll();
        for (User user : users) {
            List<Integer> filmsId = likesStorage.getFilmIdByUserId(user.getId());
            List<Film> films = new ArrayList<>();
            for (Integer integer : filmsId) {
                films.add(filmStorage.getFilmById(integer));
            }
            if (user.getId() != id) {
                diffs.put(user, films);
            } else {
                userToCompare.put(user, films);
            }
        }
    }

    public List<Film> getRecommendations(int id) {
        getInputData(id);
        int userIdWithCommonLikes = getUserIdWithCommonLikes(id);
        List<Integer> filmsId = likesStorage.getFilmIdByUserId(userIdWithCommonLikes);
        List<Integer> filmsUserId = likesStorage.getFilmIdByUserId(id);
        filmsId.removeAll(filmsUserId);
        List<Film> filmsToRecommendation = new ArrayList<>();
        for (Integer integer : filmsId) {
            filmsToRecommendation.add(filmStorage.getFilmById(integer));
        }
        log.info("Рекомендации по " + id);
        return filmsToRecommendation;
    }

    private Integer getUserIdWithCommonLikes(int id) {
        int maxCount = 0;
        int userId = id;
        for (User user : diffs.keySet()) {
            if (user.getId() != id) {
                int count = 0;
                List<Film> filmsByUser = diffs.get(user);
                for (Film film : filmsByUser) {
                    List<Film> toCompare = userToCompare.get(userStorage.getById(id).get());
                    for (Film value : toCompare) {
                        if (film.equals(value)) {
                            count += 1;
                        }
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                    userId = user.getId();
                }
             }
        }
        log.info("User с максимальным количеством пересечения по лайкам " + userId);
        return userId;
    }
}
