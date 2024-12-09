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

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.validator.Group;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmRepository repository;

    @GetMapping
    public Collection<Film> showAll() {
        log.debug("Запрошен список всех фильмов.");
        return repository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Group.Create.class) @RequestBody Film film) {
        log.debug("Запрос на создание фильма {} прошёл валидацию.", film.getName());
        return repository.add(film);
    }

    @PutMapping
    public Film update(@Validated(Group.Update.class) @RequestBody Film film) {
        log.debug("Запрос на обновление фильма {} (id={}) прошёл валидацию.", film.getName(), film.getId());
        return repository.update(film);
    }
}