package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDAO;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testData.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clean.sql")
})
public class FilmDbStorageTests {
    private final FilmDbStorage filmDbStorage;
    private final MpaDAO mpaDAO;
    Exception ex;
    Film film;


    @Test
    public void getFilmById() {
        film = filmDbStorage.getFilm(1);
        System.out.println(film);
        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "funny film")
                .hasFieldOrPropertyWithValue("description", "Very funny film")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 05, 26))
                .hasFieldOrPropertyWithValue("duration", 145);
        ;
    }

    @Test
    public void getFilmWrongId() {
        ex = assertThrows(
                NotExistException.class,
                () -> filmDbStorage.getFilm(-999)
        );
        assertEquals("Film not found", ex.getMessage());
    }

    @Test
    public void getFilms() {
        List<Film> films = filmDbStorage.getFilms();
        assertEquals(4, films.size());
        assertEquals(1, films.get(0).getId());
        assertEquals(2, films.get(1).getId());
        assertEquals(3, films.get(2).getId());
        assertEquals(4, films.get(3).getId());
    }

    @Test
    public void postFilm() {
        film = Film.builder()
                .name("new film")
                .description("...")
                .releaseDate(LocalDate.of(1999, 12, 27))
                .duration(100)
                .mpa(mpaDAO.getMpa(1))
                .build();
        filmDbStorage.postFilm(film);
        assertThat(filmDbStorage.getFilm(5))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 5)
                .hasFieldOrPropertyWithValue("name", "new film")
                .hasFieldOrPropertyWithValue("description", "...")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 12, 27))
                .hasFieldOrPropertyWithValue("duration", 100);
    }

    @Test
    public void putFilm() {
        film = Film.builder()
                .id(1)
                .name("updated film")
                .description("...")
                .releaseDate(LocalDate.of(1999, 12, 27))
                .duration(100)
                .mpa(mpaDAO.getMpa(1))
                .build();
        filmDbStorage.putFilm(film);
        assertThat(filmDbStorage.getFilm(1))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "updated film")
                .hasFieldOrPropertyWithValue("description", "...")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 12, 27))
                .hasFieldOrPropertyWithValue("duration", 100);
        assertEquals(film.getName(), filmDbStorage.getFilm(1).getName());
    }

    @Test
    public void addLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        filmDbStorage.addLike(1, 3);
        assertEquals(3, filmDbStorage.getFilm(1).getLikes().size());
        assertTrue(filmDbStorage.getFilm(1).getLikes().contains(3));
    }

    @Test
    public void addExistingLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        filmDbStorage.addLike(1, 2);
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
    }

    @Test
    public void removeLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        assertTrue(filmDbStorage.getFilm(1).getLikes().contains(2));
        filmDbStorage.removeLike(1, 2);
        assertEquals(1, filmDbStorage.getFilm(1).getLikes().size());
        assertFalse(filmDbStorage.getFilm(1).getLikes().contains(2));
    }

    @Test
    public void removeNotExistLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        assertFalse(filmDbStorage.getFilm(1).getLikes().contains(-1));
        filmDbStorage.removeLike(1, -1);
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        assertFalse(filmDbStorage.getFilm(1).getLikes().contains(-1));
    }
}
