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
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Group;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> showAll() {
        log.debug("Запрошен список всех фильмов.");
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //201
    public Film create(@Validated(Group.Create.class) @RequestBody Film film) {
        log.debug("Запрос на создание фильма {} прошёл валидацию.", film.getName());
        return service.add(film);
    }

    @PutMapping
    public Film update(@Validated(Group.Update.class) @RequestBody Film film) {
        log.debug("Запрос на обновление фильма {} (id={}) прошёл валидацию.", film.getName(), film.getId());
        return service.update(film);
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        log.debug("Запрошен фильм c id={}", filmId);
        return service.get(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на добавление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на удаление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> showPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.trace("Запрошен список популярных фильмов с count={}", count);
        return service.showPopularFilms(count);
    }

}