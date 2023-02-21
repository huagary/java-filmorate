package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDAO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MpaDAOTests {
    private final MpaDAO mpaDAO;
    private final MpaService mpaService;
    Mpa mpa;
    Exception ex;

    @Test
    public void getAllMpa() {
        assertEquals(5, mpaDAO.getAllMpa().size());
        assertEquals(1, mpaDAO.getAllMpa().get(0).getId());
        assertEquals(2, mpaDAO.getAllMpa().get(1).getId());
        assertEquals(3, mpaDAO.getAllMpa().get(2).getId());
        assertEquals(4, mpaDAO.getAllMpa().get(3).getId());
        assertEquals(5, mpaDAO.getAllMpa().get(4).getId());
    }

    @Test
    public void getMpa() {
        mpa = mpaDAO.getMpa(1);
        assertThat(mpa)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void getMpaWrongId() {
        ex = assertThrows(
                NotExistException.class,
                () -> mpaService.getMpa(-999)
        );
        assertEquals("MPA not found", ex.getMessage());
    }
}
