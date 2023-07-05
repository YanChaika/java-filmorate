DROP TABLE IF EXISTS PUBLIC.film_genre CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.film_genre (
	GENRE_ID INTEGER NOT NULL,
	GENRE_NAME VARCHAR_IGNORECASE NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (GENRE_ID)
);

DROP TABLE IF EXISTS PUBLIC.film_by_genres CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.film_by_genres (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL
);

DROP TABLE IF EXISTS PUBLIC.film_rating CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.film_rating (
	RATING_ID INTEGER NOT NULL,
	RATING_NAME CHARACTER VARYING NOT NULL,
	CONSTRAINT FILM_RATING_PK PRIMARY KEY (RATING_ID)
);

DROP TABLE IF EXISTS PUBLIC.films CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.films (
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

DROP TABLE IF EXISTS PUBLIC.users CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.users (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	USER_EMAIL VARCHAR_IGNORECASE NOT NULL,
	USER_LOGIN VARCHAR_IGNORECASE NOT NULL,
	USER_NAME VARCHAR_IGNORECASE,
	USER_BIRTHDAY DATE,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

DROP TABLE IF EXISTS PUBLIC.friends CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.friends (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID,FRIEND_ID),
	CONSTRAINT FRIENDS_FK_U FOREIGN KEY (USER_ID) REFERENCES PUBLIC.users(USER_ID) ON DELETE CASCADE,
	CONSTRAINT FRIENDS_FK_F FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.users(USER_ID) ON DELETE CASCADE
	--FOREIGN KEY (USER_ID) REFERENCES PUBLIC.users (USER_ID) ON DELETE CASCADE
);

DROP TABLE IF EXISTS PUBLIC.film_likes CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.film_likes (
	FILM_ID INTEGER NOT NULL,
	USER_ID INTEGER NOT NULL,
	CONSTRAINT LIKES_FK_F FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.films(FILM_ID) ON DELETE CASCADE,
	CONSTRAINT LIKES_FK_U FOREIGN KEY (USER_ID) REFERENCES PUBLIC.users(USER_ID) ON DELETE CASCADE
);

drop table IF EXISTS PUBLIC.eventTypes CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.eventTypes (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name VARCHAR(10) NOT NULL
);

drop table IF EXISTS PUBLIC.operations CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.operations (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name VARCHAR(10) NOT NULL
);

drop table IF EXISTS PUBLIC.feeds CASCADE;
CREATE TABLE IF NOT EXISTS PUBLIC.feeds (
        event_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        timestamp INTEGER NOT NULL,
        user_id INTEGER NOT NULL,
        eventType_id INTEGER NOT NULL,
        operation_id INTEGER NOT NULL,
        entity_id INTEGER NOT NULL,
        FOREIGN KEY (eventType_id) REFERENCES PUBLIC.eventTypes (id) ON DELETE CASCADE,
        FOREIGN KEY (operation_id) REFERENCES PUBLIC.operations (id) ON DELETE CASCADE
);