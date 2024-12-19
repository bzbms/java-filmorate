package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Repository
@Getter
@Setter
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();

    // Храним по id фильма множество id юзеров, размер множества является рейтингом популярности
    private final HashMap<Long, Set<Long>> likes = new HashMap<>();

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

    public Optional<Film> getFilm(Long id) {
        return Optional.of(films.get(id));
    }

    private long getNextId() {
        long uniqueId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }

    public HashMap<Long, Set<Long>> getLikes() {
        return likes;
    }
}
