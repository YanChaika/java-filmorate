package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.friends.FriendStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public void addFriends(Integer id, Integer friendId) {
        friendStorage.create(getById(id), getById(friendId));
    }

    public void removeFromFriends(Integer id, Integer friendId) {
        try {
            Optional<User> userToUpdate = userStorage.getById(id);
            ;
            friendStorage.remove(getById(id), getById(friendId));
        } catch (NullPointerException e) {
            throw new IncorrectIdException("Film для удаления не найден");
        }
    }

    public List<User> getAllFriends(Integer id) {
        List<User> toReturn = new ArrayList<>();
        if (userStorage.getById(id).isPresent()) {
            List<Integer> friendsById = friendStorage.getAllById(id);
            if (!friendsById.isEmpty()) {
                for (int i = 0; i < friendsById.size(); i++) {
                    toReturn.add(getById(friendsById.get(i)));
                }
            }
        }
        return toReturn;
    }

    public List<User> compareFriends(Integer id, Integer otherId) {
        List<User> toCheckUser = getAllFriends(id);
        List<User> toCheckOther = getAllFriends(otherId);
        if ((toCheckOther.isEmpty()) || (toCheckUser.isEmpty())) {
            return new ArrayList<User>();
        }
        List<User> answer = new ArrayList<>();
        for (User user : toCheckUser) {
            for (User user1 : toCheckOther) {
                if (user1.equals(user)) {
                    answer.add(user);
                }
            }
        }
        return answer;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void removeUser(Integer id) {
        userStorage.delete(id);
    }

    public User create(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (!(user.getEmail().contains("@")) ||
                (user.getLogin().contains(" ")) ||
                (user.getBirthday().isAfter(LocalDate.now()))
        ) {
            throw new ValidationException("User can't be post");
        } else {
            return userStorage.create(user);
        }
    }

    public User update(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!(user.getEmail().contains("@")) ||
                (user.getLogin().contains(" ")) ||
                (user.getBirthday().isAfter(LocalDate.now()))
        ) {
            throw new ValidationException("User can't be put");
        } else {
            return userStorage.update(user);
        }
    }

    public User getById(Integer id) {
        return userStorage.getById(id).get();
    }

}
