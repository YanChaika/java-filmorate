package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.genres.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.film.likes.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.friends.FriendDbStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final GenreDbStorage genreDbStorage;
	private final GenresDbStorage genresDbStorage;
	private final LikesDbStorage likesDbStorage;
	private final MpaDbStorage mpaDbStorage;
	private final FriendDbStorage friendDbStorage;
	private final FilmDbStorage filmDbStorage;

	@Test
	public void testGetAllGenre() {

		List<Genre> genresToCheck = genreDbStorage.getAll();

		assertThat(genresToCheck)
				.isNotNull();
	}

	@Test
	public void testFindGenreById() {

		Optional<Genre> genreToCheck = genreDbStorage.getGenreById(1);

		assertThat(genreToCheck)
				.isPresent()
				.hasValueSatisfying(genre ->
						assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testFindAllGenres() {

		List<FilmByGenres> genresToCheck = genresDbStorage.getAll();

		assertThat(genresToCheck)
				.isNotNull();
	}

	@Test
	public void testGetAllLikes() {

		List<Like> likesToCheck = likesDbStorage.getAll();

		assertThat(likesToCheck)
				.isNotNull();
	}

	@Test
	public void testGetSortedLikes() {

		Set<Like> sortedLikes = likesDbStorage.getSortedFilms();

		assertThat(sortedLikes)
				.isNotNull();
	}

	@Test
	public void testGetAllMPA() {

		List<FilmMPA> mpaToCheck = mpaDbStorage.getAllMpa();

		assertThat(mpaToCheck)
				.isNotNull();
	}

	@Test
	public void testGetMpaById() {

		FilmMPA filmMpaToCheck = mpaDbStorage.getMpaById(1);

		assertThat(filmMpaToCheck)
				.isNotNull();
	}

	@Test
	public void testGetAllFriends() {

		List<Friend> friendsToCheck = friendDbStorage.getAll();

		assertThat(friendsToCheck)
				.isNotNull();
	}

	@Test
	public void testGetFriendsId() {

		List<Friend> friendsToCheck = friendDbStorage.getAllById(1);

		assertThat(friendsToCheck)
				.isNotNull();
	}

	@Test
	public void testGetAllFilms() {

		List<Film> filmsToCheck = filmDbStorage.getAll();

		assertThat(filmsToCheck)
				.isNotNull();
	}

	@Test
	public void testGetSortedFilms() {

		List<Film> filmsSortedToCheck = filmDbStorage.getSortedFilms();

		assertThat(filmsSortedToCheck)
				.isNotNull();
	}
}
