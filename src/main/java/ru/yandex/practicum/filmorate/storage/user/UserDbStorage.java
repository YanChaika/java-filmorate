package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
            String sqlQuery = "INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY) " +
                    "VALUES(?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                        stmt.setString(1, user.getEmail());
                        stmt.setString(2, user.getLogin());
                        stmt.setString(3, user.getName());
                        stmt.setDate(4, Date.valueOf(user.getBirthday()));
                        return stmt;
                    },
                    keyHolder);

            return user.asCreated(
                    Objects.requireNonNull(keyHolder.getKey()).intValue()
            );
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        String sqlQuery = "UPDATE PUBLIC.USERS SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM PUBLIC.USERS", UserDbStorage::cons);
        if (users.isEmpty()) {
            throw new IncorrectIdException("Users is empty");
        }
        return users;
    }

    @Override
    public Optional<User> getById(Integer id) {
        final String sqlQuery = "SELECT USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY, FROM PUBLIC.USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::cons, id);

        if (users.isEmpty()) {
            throw new IncorrectIdException("user c id не найден");
        } else if (users.size() > 1) {
            throw new IllegalStateException();
        } else {
            return Optional.of(users.get(0));
        }
    }

    @Override
    public User delete(User user) {
        return null;
    }

    private static User cons(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("user_email"),
                rs.getString("user_login"),
                rs.getString("user_name"),
                rs.getDate("user_birthday").toLocalDate()
        );
    }
}