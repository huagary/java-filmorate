package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDAO;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTests {
    private final FilmDbStorage filmDbStorage;
    private final MpaDAO mpaDAO;
    Exception ex;
    Film film;

    @Test
    public void getFilmById() {
        film = filmDbStorage.getFilm(1);
        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1);
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
        assertEquals(film.getName(), filmDbStorage.getFilm(5).getName());
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
        assertEquals(film.getName(), filmDbStorage.getFilm(1).getName());
    }

    @Test
    public void addLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        filmDbStorage.addLike(1, 3);
        assertEquals(3, filmDbStorage.getFilm(1).getLikes().size());
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
        filmDbStorage.removeLike(1, 2);
        assertEquals(1, filmDbStorage.getFilm(1).getLikes().size());
    }

    @Test
    public void removeNotExistLike() {
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
        filmDbStorage.removeLike(1, -1);
        assertEquals(2, filmDbStorage.getFilm(1).getLikes().size());
    }
}
