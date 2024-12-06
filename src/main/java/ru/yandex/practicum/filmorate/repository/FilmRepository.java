package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();

    public Film add(Film film) {
        film.setId(getNextId());
        log.trace("Текущий максимальный id фильма: {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен с id {}", film.getName(), film.getId());
        return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            Film existedFilm;
            existedFilm = film;
            films.put(existedFilm.getId(), existedFilm);
            log.info("Фильм {} с id {} обновлён", film.getName(), film.getId());
            return existedFilm;
        }
        log.debug("Запрошен фильм с неизвестным id = {}", film.getId());
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден.");
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    private long getNextId() {
        long uniqueId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }
}
