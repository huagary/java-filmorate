package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {
        final String sql = "select * from mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs, -1));
    }

    public Mpa getMpa(int mpaId) {
        final String sql = "select * from mpa where mpa_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs, -1), mpaId).stream()
                .findAny().orElse(null);
    }

    private Mpa makeMpa(ResultSet rs, int num) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }
}
