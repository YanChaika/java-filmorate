package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends FilmorateApplicationTests{

    private User testUser;
    private UserController userController = new UserController();

    private void createUser() {
        testUser = new User("mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946,8,20));
    }

    @Test
    void shouldGetUsers() {
        createUser();
        userController.postUser(testUser);

        User[] users = userController.getUsers();
        assertEquals(1, users.length, "length not correct");
        Assertions.assertNotNull(users, "users is null");
        assertEquals(testUser, users[0], "user not correct");
    }

    @Test
    void shouldThrowExWhenCreateUserWithoutLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(
                        new User(
                                "mail@mail.ru",
                                null,
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be post", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenCreateUserWithoutEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(
                        new User(
                                null,
                                "dolore",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be post", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenCreateUserWithWrongEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(
                        new User(
                                "mailmail.ru",
                                "dolore",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be post", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenCreateUserWithWrongLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(
                        new User(
                                "mail@mail.ru",
                                "dolo re",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be post", exception.getMessage());
    }

    @Test
    void shouldGetUsersWithNameLikeLogin()  {
        userController.postUser(
                new User(
                "mail@mail.ru",
                "dolore",
                null,
                LocalDate.of(1946,8,20)
            )
        );

        User[] users = userController.getUsers();
        assertEquals(1, users.length, "length not correct");
        Assertions.assertNotNull(users, "users is null");
        assertEquals("dolore", users[0].getName(), "users name not correct");
    }

    @Test
    void shouldThrowExWhenCreateUserWithWrongBirthday() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.postUser(
                        new User(
                                "mail@mail.ru",
                                "dolore",
                                "Nick Name",
                                LocalDate.of(2222,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be post", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenGetEmptyUsers() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.getUsers(),
                "not correct error"
        );

        assertEquals("Users is empty", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutUserWithoutEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.putUser(
                        new User(
                                null,
                                "dolore",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be put", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutUserWithWrongEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.putUser(
                        new User(
                                "mailmail.ru",
                                "dolore",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be put", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutUserWithWrongLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.putUser(
                        new User(
                                "mail@mail.ru",
                                "dolo re",
                                "Nick Name",
                                LocalDate.of(1946,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be put", exception.getMessage());
    }

    @Test
    void shouldPutUsersWithNameLikeLogin()  {
        createUser();
        userController.postUser(testUser);
        User updatedUser = new User(
                "mail@mail.ru",
                "dolore",
                null,
                LocalDate.of(1946,8,20)
        );
        updatedUser.setId(testUser.getId());
        userController.putUser( updatedUser);

        User[] users = userController.getUsers();
        assertEquals(1, users.length, "length not correct");
        Assertions.assertNotNull(users, "users is null");
        assertEquals("dolore", users[0].getName(), "users name not correct");
    }

    @Test
    void shouldThrowExWhenPutUserWithWrongBirthday() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.putUser(
                        new User(
                                "mail@mail.ru",
                                "dolore",
                                "Nick Name",
                                LocalDate.of(2222,8,20))
                ),
                "not correct error"
        );

        assertEquals("User can't be put", exception.getMessage());
    }
}
