package ru.yandex.practicum.filmorate.storage.user.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user, User friend) {
        String sqlQuery = "INSERT INTO PUBLIC.friends (user_id, friend_id) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, user.getId());
                    stmt.setInt(2, friend.getId());
                    return stmt;
                },
                keyHolder);

        return user;
    }

    @Override
    public void remove(User user, User friend) {
        String sqlQuery = "DELETE FROM PUBLIC.friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getId(),
                friend.getId()
        );
    }

    @Override
    public List<Integer> getAllById(int id) {
        List<Integer> friendsId = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM PUBLIC.friends WHERE user_id = ?", id);
        while (userRows.next()) {
            friendsId.add(userRows.getInt("friend_id"));
        }
        return friendsId;

    }
}
