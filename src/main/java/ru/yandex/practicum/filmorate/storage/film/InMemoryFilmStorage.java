package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.GeneratorID;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final GeneratorID generatorID = new GeneratorID();

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) throw new NotExistException("Film does not exist");
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film postFilm(Film film) {
        Validator.validateFilm(film);
        film.setId(generatorID.generateId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film putFilm(Film film) {
        if (!films.containsKey(film.getId())) throw new NotExistException("Film does not exist");
        Validator.validateFilm(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).getLikes().remove(userId);
    }
}
