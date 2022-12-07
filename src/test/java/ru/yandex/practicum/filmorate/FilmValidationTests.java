package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmValidationTests {
    Film film;
    ValidationException ex;

    @BeforeEach
    void beforeEach() {
        film = new Film();
        film.setId(IdGenerator.generateFilmId());
        film.setName("nisi eiusmod");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1950, 1, 1));
        film.setDuration(100);
    }

    @AfterEach
    void afterEach() {
        IdGenerator.resetIds();
    }

    @Test
    void emptyFilmName() {
        film.setName("");
        validateThrows(film);
        assertEquals("Wrong film name format", ex.getReason());
    }

    @Test
    void blankFilmName() {
        film.setName(" ");
        validateThrows(film);
        assertEquals("Wrong film name format", ex.getReason());
    }

    @Test
    void longFilmDescription() {
        String string = "a".repeat(201);
        film.setDescription(string);
        validateThrows(film);
        assertEquals("Description should be shorter than 200 characters", ex.getReason());
    }

    @Test
    void wrongFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        validateThrows(film);
        assertEquals("Wrong release date", ex.getReason());
    }

    @Test
    void negativeFilmDuration() {
        film.setDuration(-100);
        validateThrows(film);
        assertEquals("Duration should be a positive integer", ex.getReason());
    }

    void validateThrows(Film film) {
        ex = assertThrows(
                ValidationException.class,
                () -> Validator.validateFilm(film)
        );
    }
}
