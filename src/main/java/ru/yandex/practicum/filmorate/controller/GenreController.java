package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public Collection<Genre> getAll() {
        log.debug("Совершён запрос на список всех жанров");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable Integer id) {
        log.debug("Запрос названия жанра с ID = {}", id);
        return service.get(id);
    }

    @GetMapping("/{id}/genres")
    public List<Genre> getGenresOfFilm(@PathVariable Long id) {
        log.debug("Запрошены жанры фильма с ID = {}", id);
        return service.getGenresOfFilm(id);
    }
}
