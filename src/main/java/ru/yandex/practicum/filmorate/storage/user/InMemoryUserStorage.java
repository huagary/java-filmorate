package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.GeneratorID;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    protected final Map<Integer, User> users = new HashMap<>();
    private final GeneratorID generatorID = new GeneratorID();

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) throw new NotExistException("User does not exist");
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User postUser(User user) {
        Validator.validateUser(user);
        user.setId(generatorID.generateId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User putUser(User user) {
        if (!users.containsKey(user.getId())) throw new NotExistException("User does not exist");
        Validator.validateUser(user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }
}
