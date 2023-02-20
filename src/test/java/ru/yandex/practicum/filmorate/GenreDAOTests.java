package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreDAO;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
/*@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:before.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:after.sql")
})*/
public class GenreDAOTests {
    private final GenreDAO genreDAO;
    private final GenreService genreService;
    Genre genre;
    Exception ex;

    @Test
    public void getAllGenres() {
        assertEquals(6, genreDAO.getAllGenres().size());
        assertEquals(1, genreDAO.getAllGenres().get(0).getId());
        assertEquals(2, genreDAO.getAllGenres().get(1).getId());
        assertEquals(3, genreDAO.getAllGenres().get(2).getId());
        assertEquals(4, genreDAO.getAllGenres().get(3).getId());
        assertEquals(5, genreDAO.getAllGenres().get(4).getId());
        assertEquals(6, genreDAO.getAllGenres().get(5).getId());
    }

    @Test
    public void getGenre() {
        genre = genreDAO.getGenre(1);
        assertThat(genre)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void getGenreWrongId() {
        ex = assertThrows(
                NotExistException.class,
                () -> genreService.getGenre(-999)
        );
        assertEquals("Genre not found", ex.getMessage());
    }
}
