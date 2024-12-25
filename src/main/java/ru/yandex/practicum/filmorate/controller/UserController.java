package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Group;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> showAll() {
        log.debug("Запрошен список всех пользователей.");
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Group.Create.class) @RequestBody User user) throws NotFoundException, ValidationException {
        log.debug("Запрос на создание пользователя {} прошёл валидацию.", user.getLogin());
        return service.add(user);
    }

    @PutMapping
    public User update(@Validated(Group.Update.class) @RequestBody User user) {
        log.debug("Запрос на обновление пользователя {} (id={}) прошёл валидацию.", user.getLogin(), user.getId());
        return service.update(user)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", user.getId())));
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Long userId) {
        log.debug("Запрошен пользователь c id={}", userId);
        return service.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.debug("От пользователя с id={} совершён запрос на добавление друга id={} ", userId, friendId);
        service.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        service.get(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        if (service.addFriend(userId, userId)) {
            log.info("Пользователи id={} и id={} теперь друзья", userId, friendId);
        } else {
            log.trace("Пользователь {} попытался стать сам себе другом :В", userId);
        }
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.trace("От пользователя с id={} совершён запрос на удаление друга id={} ", userId, friendId);
        service.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        service.get(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        if (service.deleteFriend(userId, userId)) {
            log.info("Пользователи id={} и id={} теперь не числятся в друзьях", userId, friendId);
        } else {
            log.trace("У пользователя с id={} не был удалён друг с id={}", userId, friendId);
        }
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> showFriendsByUser(@PathVariable Long userId) {
        log.trace("Запрошен список друзей пользователя с id={}", userId);
        return service.showFriendsByUser(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> showFriendsByUser(@PathVariable Long userId, @PathVariable Long otherId) {
        log.trace("Запрошен список общих друзей от пользователя с id={} и id={}", userId, otherId);
        return service.showFriendsCommonWithUser(userId, otherId);
    }

}