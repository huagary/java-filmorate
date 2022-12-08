package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTests {
    User user;
    ValidationException ex;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(IdGenerator.generateUid());
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @AfterEach
    void afterEach() {
        IdGenerator.resetIds();
    }

    @Test
    void emptyUserEmail() {
        user.setEmail("");
        validateThrows(user);
        assertEquals("Wrong e-mail format", ex.getReason());
    }

    @Test
    void wrongUserEmail() {
        user.setEmail("mail.mail.ru");
        validateThrows(user);
        assertEquals("Wrong e-mail format", ex.getReason());
    }

    @Test
    void emptyUserLogin() {
        user.setLogin("");
        validateThrows(user);
        assertEquals("Login is empty or contains spaces", ex.getReason());
    }

    @Test
    void userLoginContainsSpaces() {
        user.setLogin("dolore ullamco");
        validateThrows(user);
        assertEquals("Login is empty or contains spaces", ex.getReason());
    }

    @Test
    void userBirthdayInFuture() {
        user.setBirthday(LocalDate.of(2025, 1, 1));
        validateThrows(user);
        assertEquals("Birthday is after then Now", ex.getReason());
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
