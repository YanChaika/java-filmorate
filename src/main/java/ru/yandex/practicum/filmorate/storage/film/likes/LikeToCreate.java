package ru.yandex.practicum.filmorate.storage.film.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Like;

@Getter
@AllArgsConstructor
public class LikeToCreate {

    private final int filmId;
    private final int userId;

    public Like asCreated(int filmId, int userId) {
        return new Like(filmId, userId);
    }
}
