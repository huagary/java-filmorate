package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.GeneratorID;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTests {
    Film film;
    ValidationException ex;
    GeneratorID generatorID;

    @BeforeEach
    void beforeEach() {
        film = new Film();
        generatorID = new GeneratorID();
        film.setId(generatorID.generateId());
        film.setName("nisi eiusmod");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1950, 1, 1));
        film.setDuration(100);
    }

    @Test
    void emptyFilmName() {
        film.setName("");
        validateThrows(film);
        assertEquals("Film name is empty or blank", ex.getMessage());
    }

    @Test
    void blankFilmName() {
        film.setName(" ");
        validateThrows(film);
        assertEquals("Film name is empty or blank", ex.getMessage());
    }

    @Test
    void longFilmDescription() {
        String string = "a".repeat(201);
        film.setDescription(string);
        validateThrows(film);
        assertEquals("Description should be shorter than 200 characters", ex.getMessage());
    }

    @Test
    void wrongFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        validateThrows(film);
        assertEquals("Wrong release date", ex.getMessage());
    }

    @Test
    void negativeFilmDuration() {
        film.setDuration(-100);
        validateThrows(film);
        assertEquals("Duration should be a positive integer", ex.getMessage());
    }

    void validateThrows(Film film) {
        ex = assertThrows(
                ValidationException.class,
                () -> Validator.validateFilm(film)
        );
    }
}
