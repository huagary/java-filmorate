package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final String gotReq = "Получен запрос к эндпоинту: '{} {}'";
    private final String friendsEndpoint = "/{id}/friends";


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.getUser(id);
    }

    @GetMapping(friendsEndpoint)
    public List<User> getFriends(@PathVariable Integer id,
                                 HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.getFriends(id);
    }

    @GetMapping(friendsEndpoint + "/common/{otherId}")
    public List<User> commonFriends(@PathVariable Integer id,
                                    @PathVariable Integer otherId,
                                    HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.commonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody User user,
                           HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.postUser(user);
    }

    @PutMapping
    public User putUser(@RequestBody User user,
                        HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        return userService.putUser(user);
    }

    @PutMapping(friendsEndpoint + "/{friendId}")
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId,
                          HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(friendsEndpoint + "/{friendId}")
    public void removeFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId,
                             HttpServletRequest request) {
        log.info(gotReq, request.getMethod(), request.getRequestURI());
        userService.removeFriend(id, friendId);
    }
}
