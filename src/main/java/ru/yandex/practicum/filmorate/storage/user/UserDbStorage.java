package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        final String sql = "insert into friendship (user_id, friend_id) values(?,?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        final String sql = "delete from friendship where user_id=? and friend_id=?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public User getUser(int id) {
        final String sql = "select * from users where user_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            throw new NotExistException("User not found");
        } else return makeUser(rs, id);
    }

    @Override
    public List<User> getUsers() {
        final String sql = "select * from users";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(makeUser(rs, rs.getInt("user_id")));
        }
        return users;
    }

    @Override
    public User postUser(User newUser) {
        Validator.validateUser(newUser);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        final String sql = "insert into users (name, email, login, birthday) values (?,?,?,?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(2, newUser.getEmail());
                    ps.setString(3, newUser.getLogin());
                    ps.setString(1, newUser.getName());
                    ps.setDate(4, Date.valueOf(newUser.getBirthday()));
                    return ps;
                }, generatedKeyHolder);
        if (newUser.getFriends() != null)
            postFriends(generatedKeyHolder.getKey().intValue(), newUser.getFriends());
        if (newUser.getFriendRequests() != null)
            postRequests(generatedKeyHolder.getKey().intValue(), newUser.getFriendRequests());
        return getUser(generatedKeyHolder.getKey().intValue());
    }

    @Override
    public User putUser(User updatedUser) {
        getUser(updatedUser.getId());
        Validator.validateUser(updatedUser);
        final String sql = "update users set email=?, login=?, name=?, birthday=?";
        jdbcTemplate.update(sql,
                updatedUser.getEmail(),
                updatedUser.getLogin(),
                updatedUser.getName(),
                updatedUser.getBirthday());
        return getUser(updatedUser.getId());
    }

    private User makeUser(SqlRowSet rs, int userId) {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Objects.requireNonNull(rs.getDate("birthday")).toLocalDate())
                .friends(getFriendIds(userId))
                .friendRequests((getRequests(userId)))
                .build();
    }

    private Set<Integer> getFriendIds(int userId) {
        String sql = "select friend_id from friendship where user_id=?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId));
    }


    private Set<Integer> getRequests(int userId) {
        String sql = "select user_id from friendship where friend_id=?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId));
    }

    private void postFriends(int userId, Set<Integer> friends) {
        final String sql = "insert into friendship (user_id, friend_id) values(?,?)";
        for (Integer friendId : friends) {
            jdbcTemplate.update(sql, userId, friendId);
        }
    }

    private void postRequests(int userId, Set<Integer> friends) {
        final String sql = "insert into friendship (user_id, friend_id) values(?,?)";
        for (Integer friendId : friends) {
            jdbcTemplate.update(sql, userId, friendId);
        }
    }
}
