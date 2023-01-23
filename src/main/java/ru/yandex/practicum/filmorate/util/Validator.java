package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {
    public static void validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Неправильный формат e-mail: '{}'", user.getEmail());
            throw new ValidationException("Wrong e-mail format");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Логин пустой или содержит пробелы: '{}'", user.getLogin());
            throw new ValidationException("Login is empty or contains spaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения указана в будущем: '{}'", user.getBirthday());
            throw new ValidationException("Birthday is after then Now");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя изменено на логин: '{}'", user.getName());
        }
    }

    public static void validateFilm(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Название фильма пустое или содержит пробелы: '{}'", film.getName());
            throw new ValidationException("Film name is empty or blank");
        }
        if (film.getDescription().length() > 200) {
            log.warn(
                    "Описание должно быть короче 200 символов: '{}'",
                    film.getDescription().length()
            );
            throw new ValidationException("Description should be shorter than 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза не должна быть раньше '1895-12-28' - даты возникновения первого фильма: '{}'",
                    film.getReleaseDate());
            throw new ValidationException("Wrong release date");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность должна быть положительным целым числом: '{}'", film.getDuration());
            throw new ValidationException("Duration should be a positive integer");
        }
    }
}
