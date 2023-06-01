package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(Integer id, Integer friendId) {
        User userToUpdate = userStorage.getById(id);
        User friendToUpdate = userStorage.getById(friendId);
        Set<Integer> friendsUser1 = userToUpdate.getFriends();
        if (friendsUser1 == null) {
            friendsUser1 = new HashSet<>();
        }
        friendsUser1.add(friendId);
        userToUpdate.setFriends(friendsUser1);
        userStorage.update(userToUpdate);
        Set<Integer> friendsUser2 = friendToUpdate.getFriends();
        if (friendsUser2 == null) {
            friendsUser2 = new HashSet<>();
        }
        friendsUser2.add(id);
        friendToUpdate.setFriends(friendsUser2);
        userStorage.update(friendToUpdate);
        return userToUpdate;
    }

    public User removeFromFriends(Integer id, Integer friendId) {
        User userToUpdate = userStorage.getById(id);
        User friendToUpdate = userStorage.getById(friendId);
        Set<Integer> friendsUser1 = userToUpdate.getFriends();
        friendsUser1.remove(friendId);
        userToUpdate.setFriends(friendsUser1);
        userStorage.update(userToUpdate);
        Set<Integer> friendsUser2 = friendToUpdate.getFriends();
        friendsUser2.remove(friendId);
        friendToUpdate.setFriends(friendsUser2);
        userStorage.update(friendToUpdate);
        return userToUpdate;
    }

    public List<User> getAllFriends(Integer id) {
        List<User> toReturn = new ArrayList<>();
        if (userStorage.getById(id).getFriends() != null) {
            if (!userStorage.getById(id).getFriends().isEmpty()) {
                for (Integer friend : userStorage.getById(id).getFriends()) {
                    toReturn.add(userStorage.getById(friend));
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
}