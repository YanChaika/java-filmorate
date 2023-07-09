package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationsService {

    private final LikesStorage likesStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private static Map<User, List<Film>> diffs = new HashMap<>();
    private static Map<User, List<Film>> userToCompare = new HashMap<>();

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
        Set<Integer> filmsIdToRecommendation = new HashSet<>();
        for (int i = 0; i < filmsId.size(); i++) {
            for (int i1 = 0; i1 < filmsUserId.size(); i1++) {
                if (filmsId.get(i) == (filmsUserId.get(i1))) {
                    filmsIdToRecommendation.remove(filmsId.get(i));
                    break;
                } else {
                    filmsIdToRecommendation.add(filmsId.get(i));
                }
            }
        }
        List<Film> filmsToRecommendation = new ArrayList<>();
        for (Integer integer : filmsIdToRecommendation) {
            filmsToRecommendation.add(filmStorage.getFilmById(integer));
        }
        return filmsToRecommendation;
    }

    private Integer getUserIdWithCommonLikes(int id) {
        int maxCount = 0;
        int userId = id;
        for (User user : diffs.keySet()) {
            if (user.getId() != id) {
                int count = 0;
                List<Film> filmsByUser = diffs.get(user);
                for (int i = 0; i < filmsByUser.size(); i++) {
                    List<Film> toCompare = userToCompare.get(userStorage.getById(id).get());
                    for (int i1 = 0; i1 < toCompare.size(); i1++) {
                        if (filmsByUser.get(i).equals(toCompare.get(i1))) {
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
        return userId;
    }
}
