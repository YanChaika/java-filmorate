package ru.yandex.practicum.filmorate.storage.user.friends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Friend;
@Getter
@AllArgsConstructor
public class FriendToCreate {

    private final int userId;
    private final int friendId;

    public Friend asCreated(int userId, int friendId) {
            return new Friend(userId, friendId);
    }
}
