package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private Film testFilm;

    private FilmController filmController = new FilmController();

    private void createFilm() {
        testFilm = new Film(
                "nisi eiusmod",
                "adipisicing",
                LocalDate.of(1946,8,20),
                100
        );
    }


    @Test
    void shouldGetFilms() {
        createFilm();
        filmController.postFilm(testFilm);

        Film[] films = filmController.getFilms();
        assertEquals(1, films.length, "length not correct");
        Assertions.assertNotNull(films, "films is null");
        assertEquals(testFilm, films[0], "film not correct");
    }

    @Test
    void shouldThrowExWhenCreateFilmWithoutName() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> filmController.postFilm(
                        new Film(
                                null,
                                "adipisicing",
                                LocalDate.of(1946,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowExWhenCreateFilmWithTooLongDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.postFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing kljdfjksdjlsldjgjkdsjkgljslkdjklslkjkkksiqwjkjsdkjgnskjdfhioaoiwehrjwdkdskafhsdgi0jwoejnradslkmfdajfbguadhfjgaas'lkadfaksldhgjdkfhgdsklfjqkjkjdlskjfklsjdlkfjskdjfljsdlkfjslkdjfjlksdlfkjsldkjfklsjdfjsdlkfjklsdjfkljsdkjf",
                                LocalDate.of(1946,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be post film", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenCreateFilmWithDateBeforeStartDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.postFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing",
                                LocalDate.of(1446,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be post film", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenCreateFilmWithDurationBelowNull() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.postFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing",
                                LocalDate.of(1946,8,20),
                                -100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be post film", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutFilmWithoutName() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> filmController.putFilm(
                        new Film(
                                null,
                                "adipisicing",
                                LocalDate.of(1946,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertNotNull(exception);
    }

    @Test
    void shouldThrowExWhenPutFilmWithTooLongDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.putFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing kljdfjksdjlsldjgjkdsjkgljslkdjklslkjkkksiqwjkjsdkjgnskjdfhioaoiwehrjwdkdskafhsdgi0jwoejnradslkmfdajfbguadhfjgaas'lkadfaksldhgjdkfhgdsklfjqkjkjdlskjfklsjdlkfjskdjfljsdlkfjslkdjfjlksdlfkjsldkjfklsjdfjsdlkfjklsdjfkljsdkjf",
                                LocalDate.of(1946,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be put film", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutFilmWithDateBeforeStartDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.putFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing",
                                LocalDate.of(1446,8,20),
                                100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be put film", exception.getMessage());
    }

    @Test
    void shouldThrowExWhenPutFilmWithDurationBelowNull() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.putFilm(
                        new Film(
                                "nisi eiusmod",
                                "adipisicing",
                                LocalDate.of(1946,8,20),
                                -100
                        )
                ),
                "not correct error"
        );

        assertEquals("Error: can't be put film", exception.getMessage());
    }
}
