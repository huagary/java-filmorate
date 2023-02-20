package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(int id);

    List<Film> getFilms();

    Film postFilm(Film film);

    Film putFilm(Film film);
}
