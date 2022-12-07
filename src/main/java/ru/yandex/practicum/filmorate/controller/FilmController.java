package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        Validator.validateFilm(film);
        film.setId(IdGenerator.generateFilmId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        if (!films.containsKey(film.getId())) throw new RuntimeException();
        Validator.validateFilm(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }
}
