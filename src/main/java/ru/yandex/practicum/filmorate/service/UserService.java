package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User postUser(User user) {
        return userStorage.postUser(user);
    }

    public User putUser(User user) {
        return userStorage.putUser(user);
    }

    public void addFriend(int userId, int friendId) {
        if (userStorage.getUser(userId) == null) throw new NotExistException("User id not found");
        if (userStorage.getUser(friendId) == null) throw new NotExistException("Friend id not found");
        userStorage.addFriend(userId, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getUsers().stream()
                .filter(f -> userStorage.getUser(id).getFriends().contains(f.getId()))
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public void removeFriend(int userId, int friendId) {
        if (userStorage.getUser(userId) == null) throw new NotExistException("User id not found");
        if (userStorage.getUser(friendId) == null) throw new NotExistException("Friend id not found");
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> commonFriends(int userId, int otherId) {
        Set<Integer> userFriends = new HashSet<>(userStorage.getUser(userId).getFriends());
        Set<Integer> otherFriends = new HashSet<>(userStorage.getUser(otherId).getFriends());
        return otherFriends.stream()
                .filter(userFriends::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
