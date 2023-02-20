package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;
    Exception ex;
    User user;

    @Test
    public void getUserById() {
        user = userDbStorage.getUser(1);
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Quentin Tarantino")
                .hasFieldOrPropertyWithValue("email", "tarantino@ya.ru")
                .hasFieldOrPropertyWithValue("login", "tarantino")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1963, 03, 27));
        ;
    }

    @Test
    public void getUserWrongId() {
        ex = assertThrows(
                NotExistException.class,
                () -> userDbStorage.getUser(-999)
        );
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    public void getUsers() {
        List<User> users = userDbStorage.getUsers();
        assertEquals(3, users.size());
        assertEquals(1, users.get(0).getId());
        assertEquals(2, users.get(1).getId());
        assertEquals(3, users.get(2).getId());
    }

    @Test
    public void postUsers() {
        user = User.builder()
                .name("new user")
                .email("user@email.com")
                .login("user")
                .birthday(LocalDate.of(1985, 12, 15))
                .build();
        userDbStorage.postUser(user);
        assertThat(userDbStorage.getUser(4))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("name", "new user")
                .hasFieldOrPropertyWithValue("email", "user@email.com")
                .hasFieldOrPropertyWithValue("login", "user")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, 12, 15));
    }

    @Test
    public void putUser() {
        user = User.builder()
                .id(1)
                .name("updated user")
                .email("upd.user@email.com")
                .login("upd_user")
                .birthday(LocalDate.of(1985, 12, 15))
                .build();
        userDbStorage.putUser(user);
        assertThat(userDbStorage.getUser(1))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "updated user")
                .hasFieldOrPropertyWithValue("email", "upd.user@email.com")
                .hasFieldOrPropertyWithValue("login", "upd_user")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1985, 12, 15));
    }

    @Test
    public void addFriend() {
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        userDbStorage.addFriend(2, 1);
        assertEquals(2, userDbStorage.getUser(2).getFriends().size());
        assertTrue(userDbStorage.getUser(2).getFriends().contains(1));
    }

    @Test
    public void addFriendThemself() {
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        userDbStorage.addFriend(2, 2);
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        assertFalse(userDbStorage.getUser(2).getFriends().contains(2));
    }

    @Test
    public void removeFriend() {
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        assertTrue(userDbStorage.getUser(2).getFriends().contains(3));
        userDbStorage.removeFriend(2, 3);
        assertEquals(0, userDbStorage.getUser(2).getFriends().size());
        assertFalse(userDbStorage.getUser(2).getFriends().contains(3));
    }

    @Test
    public void removeNotExistingFriend() {
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        assertFalse(userDbStorage.getUser(2).getFriends().contains(1));
        userDbStorage.removeFriend(2, 1);
        assertEquals(1, userDbStorage.getUser(2).getFriends().size());
        assertFalse(userDbStorage.getUser(2).getFriends().contains(1));
    }
}
