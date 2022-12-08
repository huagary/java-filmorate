package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {
    public static void validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Wrong e-mail format: '{}'", user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Wrong e-mail format");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Login is empty or contains spaces: '{}'", user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Login is empty or contains spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday is after then Now: '{}'", user.getBirthday());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Birthday is after then Now");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("User name changed to login: '{}'", user.getName());
        }
    }

    public static void validateFilm(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Film name is empty or blank: '{}'", film.getName());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Wrong film name format");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Description should be shorter than 200 characters, now it is: '{}'", film.getDescription().length());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Description should be shorter than 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Wrong release date: '{}'", film.getReleaseDate());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Wrong release date");
        }
        if (film.getDuration() < 0) {
            log.warn("Duration should be a positive integer");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Duration should be a positive integer");
        }
    }
}
