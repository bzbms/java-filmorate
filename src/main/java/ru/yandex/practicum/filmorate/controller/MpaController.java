package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping
    public Collection<Mpa> getAll() {
        log.debug("Совершён запрос на список всех возрастных рейтингов");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable Integer id) {
        log.debug("Запрос названия рейтинга с ID = {}", id);
        return service.get(id);
    }
}