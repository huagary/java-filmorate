--DROP TABLE mpa, film, genre, film_genre, users, film_likes, friendship CASCADE;

CREATE TABLE IF NOT EXISTS mpa  (
	mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name NVARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS film (
	film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name NVARCHAR(100) NOT NULL,
	description NVARCHAR(200),
	release_date DATE NOT NULL,
	duration INTEGER NOT NULL,
	mpa_id INTEGER REFERENCES mpa (mpa_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS genre (
	genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name NVARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id INTEGER REFERENCES film (film_id) ON DELETE CASCADE,
	genre_id INTEGER REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
	user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	email NVARCHAR(100) NOT NULL,
	login NVARCHAR(50) NOT NULL,
	name NVARCHAR(50) NOT NULL,
	birthday DATE 
);

CREATE TABLE IF NOT EXISTS film_likes (
	film_id INTEGER REFERENCES film (film_id) ON DELETE CASCADE,
	user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship (
	user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
	friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
);

ALTER TABLE mpa ADD CONSTRAINT IF NOT EXISTS chk_name_val CHECK (name IN ('G', 'PG', 'PG-13', 'R', 'NC-17'));

DELETE FROM friendship;
DELETE FROM film_likes;
DELETE FROM film_genre;
DELETE FROM film;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;
DELETE FROM users;
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
DELETE FROM mpa;
ALTER TABLE mpa ALTER COLUMN mpa_id RESTART WITH 1;
DELETE FROM genre;
ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 1;


