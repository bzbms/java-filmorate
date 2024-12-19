package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Group;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmService service;

    @GetMapping
    public Collection<Film> showAll() {
        log.debug("Запрошен список всех фильмов.");
        return filmRepository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //201
    public Film create(@Validated(Group.Create.class) @RequestBody Film film) {
        log.debug("Запрос на создание фильма {} прошёл валидацию.", film.getName());
        return filmRepository.add(film);
    }

    @PutMapping
    public Film update(@Validated(Group.Update.class) @RequestBody Film film) {
        log.debug("Запрос на обновление фильма {} (id={}) прошёл валидацию.", film.getName(), film.getId());
        return filmRepository.update(film);
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        log.debug("Запрошен фильм c id={}", filmId);
        return filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на добавление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") Long filmId, @PathVariable("userId") Long userId) {
        log.trace("Запрос на удаление лайка фильму с id={} от пользователя с id={}.", filmId, userId);
        filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> showPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.trace("Запрошен список популярных фильмов с count={}", count);
        return service.showPopularFilms(count);
    }

}