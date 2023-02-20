package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDAO;
import ru.yandex.practicum.filmorate.util.Validator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDAO genreDAO;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDAO genreDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDAO = genreDAO;
    }

    @Override
    public Film getFilm(int id) {
        final String sql = "select * from film where film_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            throw new NotExistException("Film not found");
        } else
            return makeFilm(rs, id);
    }

    @Override
    public List<Film> getFilms() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from film");
        List<Film> films = new ArrayList<>();
        while (rs.next()) {
            films.add(makeFilm(rs, rs.getInt("film_id")));
        }
        return films;
    }

    @Override
    public Film postFilm(Film newFilm) {
        Validator.validateFilm(newFilm);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        final String sql = "insert into film (name, description, release_date, duration, mpa_id) values(?,?,?,?,?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, newFilm.getName());
                    ps.setString(2, newFilm.getDescription());
                    ps.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
                    ps.setInt(4, newFilm.getDuration());
                    ps.setInt(5, newFilm.getMpa().getId());
                    return ps;
                }, generatedKeyHolder);
        if (newFilm.getGenres() != null) postFilmGenres(generatedKeyHolder.getKey().intValue(), newFilm.getGenres());
        return getFilm(generatedKeyHolder.getKey().intValue());
    }

    @Override
    public Film putFilm(Film updatedFilm) {
        getFilm(updatedFilm.getId());
        Validator.validateFilm(updatedFilm);
        final String sql = "update film set name=?, description=?, release_date=?, duration=?, mpa_id=? where film_id=?";
        jdbcTemplate.update(sql,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId());
        if (updatedFilm.getGenres() != null) postFilmGenres(updatedFilm.getId(), updatedFilm.getGenres());
        return getFilm(updatedFilm.getId());
    }

    @Override
    public void addLike(int filmId, int userId) {
        final String sql = "insert into film_likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        final String sql = "delete from film_likes where film_id=? and user_id=?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private Film makeFilm(SqlRowSet rs, int id) {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(Objects.requireNonNull(rs.getDate("release_date")).toLocalDate())
                .duration(rs.getInt("duration"))
                .likes(getLikes(id))
                .genres(getGenres(id))
                .mpa(makeMpa(rs.getInt("mpa_id")))
                .build();
    }

    private Set<Integer> getLikes(int filmId) {
        final String sql = "select user_id from film_likes where film_id=?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, filmId));
    }

    private Set<Genre> getGenres(int filmId) {
        final String sql = "select * from genre where genre_id in " +
                "(select genre_id from film_genre where film_id=?)";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> genreDAO.makeGenre(rs, -1), filmId));
    }

    private void postFilmGenres(int filmId, Set<Genre> genres) {
        final String delSql = "delete from film_genre where film_id=?";
        jdbcTemplate.update(delSql, filmId);
        final String postSql = "insert into film_genre (film_id, genre_id) values(?,?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(postSql, filmId, genre.getId());
        }
    }

    private Mpa makeMpa(int mpaId) {
        final String mpaName = jdbcTemplate.queryForObject(
                "select name from mpa where mpa_id=?",
                String.class,
                mpaId
        );
        return Mpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
    }
}
