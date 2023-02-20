package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }

    public boolean addLike(int filmId, int userId) {
        return filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    public boolean removeLike(int filmId, int userId) {
        if (filmStorage.getFilm(filmId) == null) throw new NotExistException("Film does not exist");
        if (!filmStorage.getFilm(filmId).getLikes().contains(userId))
            throw new NotExistException("Like does not exist");
        return filmStorage.getFilm(filmId).getLikes().remove(userId);
    }

    public List<Film> popular(int count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
