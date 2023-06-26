DROP TABLE IF EXISTS PUBLIC.FILM_GENRE CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRE (
	GENRE_ID INTEGER NOT NULL,
	GENRE_NAME VARCHAR_IGNORECASE NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (GENRE_ID)
);

DROP TABLE IF EXISTS PUBLIC.FILM_BY_GENRES CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FILM_BY_GENRES (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL
);

DROP TABLE IF EXISTS PUBLIC.FILM_RATING CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FILM_RATING (
	RATING_ID INTEGER NOT NULL,
	RATING_NAME CHARACTER VARYING NOT NULL,
	CONSTRAINT FILM_RATING_PK PRIMARY KEY (RATING_ID)
);

DROP TABLE IF EXISTS PUBLIC.FILMS CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
	FILM_NAME VARCHAR_IGNORECASE NOT NULL,
	DESCRIPTION VARCHAR_IGNORECASE,
	RELEASE_DATE DATE,
	DURATION INTEGER NOT NULL,
	--LIKES_ID INTEGER,
	--COUNT_LIKES INTEGER,
	GENRE_ID INTEGER,
	RATING_ID INTEGER NOT NULL,
	--CONSTRAINT FILMS_FK PRIMARY KEY (GENRE_ID,GENRE_ID),
	--CONSTRAINT FILMS_FK_1 PRIMARY KEY (RATING_ID,RATING_ID),
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID)
	--CONSTRAINT FILMS_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.FILM_GENRE(GENRE_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	--CONSTRAINT FILMS_FK_1 FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.FILM_RATING(RATING_ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);

DROP TABLE IF EXISTS PUBLIC.FRIENDS CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL
);

DROP TABLE IF EXISTS PUBLIC.USERS CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	USER_EMAIL VARCHAR_IGNORECASE NOT NULL,
	USER_LOGIN VARCHAR_IGNORECASE NOT NULL,
	USER_NAME VARCHAR_IGNORECASE,
	USER_BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

DROP TABLE IF EXISTS PUBLIC.FILM_LIKES CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.FILM_LIKES (
	FILM_ID INTEGER NOT NULL,
	USER_ID INTEGER NOT NULL
);