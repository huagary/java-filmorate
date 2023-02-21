package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final String gotReq = "Получен запрос к эндпоинту: '{} {}'";
    private final String likeEndpoint = "/{id}/like/{userId}";

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms(HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer id,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> popular(@RequestParam(defaultValue = "10", required = false) Integer count,
                              HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        if (count <= 0) throw new IncorrectParameterException("param count is negative or 0");
        return filmService.popular(count);
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film,
                         HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return filmService.postFilm(film);
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return filmService.putFilm(film);
    }

    @PutMapping(likeEndpoint)
    public void addLike(@PathVariable Integer id,
                        @PathVariable Integer userId,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        filmService.addLike(id, userId);
    }

    @DeleteMapping(likeEndpoint)
    public void removeLike(@PathVariable Integer id,
                           @PathVariable Integer userId,
                           HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        filmService.removeLike(id, userId);
    }
}
