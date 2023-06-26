package ru.yandex.practicum.filmorate.storage.user.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FriendDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Friend create(FriendToCreate friend) {
        String sqlQuery = "INSERT INTO PUBLIC.FRIENDS (user_id, friend_id) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                    stmt.setInt(1, friend.getUserId());
                    stmt.setInt(2, friend.getFriendId());
                    return stmt;
                },
                keyHolder);

        return friend.asCreated(friend.getUserId(), friend.getFriendId());
    }

    public void remove(Friend friend) {
        String sqlQuery = "DELETE FROM PUBLIC.FRIENDS WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(
                sqlQuery,
                friend.getUserId(),
                friend.getFriendId()
        );
    }

    public List<Friend> getAll() {
        return jdbcTemplate.query("SELECT USER_ID, FRIEND_ID FROM PUBLIC.FRIENDS ", FriendDbStorage::cons);
    }

    public List<Friend> getAllById(int id) {
        return jdbcTemplate.query(
                "SELECT user_id, friend_id FROM PUBLIC.FRIENDS WHERE user_id = ?",
                FriendDbStorage::cons, id
        );
    }

    private static Friend cons(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(
                rs.getInt("user_id"),
                rs.getInt("friend_id")
        );
    }

}
