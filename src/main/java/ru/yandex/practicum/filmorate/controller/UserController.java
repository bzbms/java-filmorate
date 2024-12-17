package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Group;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository repository;
    private final UserService service;

    @GetMapping
    public Collection<User> showAll() {
        log.debug("Запрошен список всех пользователей.");
        return repository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Group.Create.class) @RequestBody User user) {
        log.debug("Запрос на создание пользователя {} прошёл валидацию.", user.getLogin());
        return repository.add(user);
    }

    @PutMapping
    public User update(@Validated(Group.Update.class) @RequestBody User user) {
        log.debug("Запрос на обновление пользователя {} (id={}) прошёл валидацию.", user.getLogin(), user.getId());
        return repository.update(user);
    }
}