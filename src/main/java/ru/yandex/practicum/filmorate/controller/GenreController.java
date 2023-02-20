package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final String gotReq = "Получен запрос к эндпоинту: '{} {}'";

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres(HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getMpa(@PathVariable("id") Integer id,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return genreService.getGenre(id);
    }
}
