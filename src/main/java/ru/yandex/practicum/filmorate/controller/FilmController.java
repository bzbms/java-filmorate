package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Group;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;
    private final UserService userService;

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
        return service.update(film)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", film.getId())));
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        log.debug("Запрошен фильм c id={}", filmId);
        return service.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на добавление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        service.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userService.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        if (service.addLike(filmId, userId)) {
            log.debug("Лайк фильму c id={} добавлен от пользователя c id={}", filmId, userId);
        } else {
            log.trace("Лайк уже был добавлен фильму c id={} от пользователя c id={}", filmId, userId);
        }
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на удаление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        service.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userService.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        if (service.deleteLike(filmId, userId)) {
            log.debug("Лайк фильму c id={} удалён от пользователя c id={}", filmId, userId);
        } else {
            log.trace("Удалить лайк у фильму c id={} от пользователя c id={} не удалось - он не поставлен", filmId, userId);
        }
    }

    @GetMapping("/popular")
    public Collection<Film> showPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.trace("Запрошен список популярных фильмов с count={}", count);
        return service.showPopularFilms(count);
    }

}