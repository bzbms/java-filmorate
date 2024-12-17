package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmRepository implements FilmRepository {
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
            Film existedFilm = films.get(film.getId());
            if (film.getName() != null && !film.getName().isBlank()) {
                existedFilm.setName(film.getName());
                log.debug("Название фильма {} было обновлено.", film.getId());
            }
            if (film.getDescription() != null && !film.getDescription().isBlank()) {
                existedFilm.setDescription(film.getDescription());
                log.debug("Описание фильма {} было обновлено.", film.getId());
            }
            if (film.getReleaseDate() != null) {
                existedFilm.setReleaseDate(film.getReleaseDate());
                log.debug("Дата релиза фильма {} была обновлена.", film.getId());
            }
            if (existedFilm.getDuration() != film.getDuration()) {
                existedFilm.setDuration(film.getDuration());
                log.debug("Длительность фильма {} была обновлена.", film.getId());
            }
            films.put(existedFilm.getId(), existedFilm);
            log.info("Фильм {} с id {} обновлён", existedFilm.getName(), existedFilm.getId());
            return existedFilm;
        }
        log.debug("Запрошен фильм с неизвестным id = {}", film.getId());
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден.");
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film get(Long id) {
        if (films.containsKey(id)) {
            log.trace("Запрошен фильм с id = {}", id);
            return films.get(id);
        } else {
            log.debug("Запрошен фильм с неизвестным id = {}", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
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
