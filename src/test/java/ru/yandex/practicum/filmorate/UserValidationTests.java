package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.GeneratorID;

import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTests {
    User user;
    ValidationException ex;
    GeneratorID generatorID;

    @BeforeEach
    void beforeEach() {
        user = new User();
        generatorID = new GeneratorID();
        user.setId(generatorID.generateId());
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void emptyUserEmail() {
        user.setEmail("");
        validateThrows(user);
        assertEquals("Wrong e-mail format", ex.getMessage());
    }

    @Test
    void wrongUserEmail() {
        user.setEmail("mail.mail.ru");
        validateThrows(user);
        assertEquals("Wrong e-mail format", ex.getMessage());
    }

    @Test
    void emptyUserLogin() {
        user.setLogin("");
        validateThrows(user);
        assertEquals("Login is empty or contains spaces", ex.getMessage());
    }

    @Test
    void userLoginContainsSpaces() {
        user.setLogin("dolore ullamco");
        validateThrows(user);
        assertEquals("Login is empty or contains spaces", ex.getMessage());
    }

    @Test
    void userBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2025, 1, 1));
        validateThrows(user);
        assertEquals("Birthday is after then Now", ex.getMessage());
    }

    @Test
    void emptyUserName() {
        user.setName("");
        Validator.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    void validateThrows(User user) {
        ex = assertThrows(
                ValidationException.class,
                () -> Validator.validateUser(user)
        );
    }
}
