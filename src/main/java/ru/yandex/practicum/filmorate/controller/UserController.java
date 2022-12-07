package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    protected final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        Validator.validateUser(user);
        user.setId(IdGenerator.generateUid());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @PutMapping
    public User putUser(@RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        if (!users.containsKey(user.getId())) throw new RuntimeException("User does not exist");
        Validator.validateUser(user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }
}
