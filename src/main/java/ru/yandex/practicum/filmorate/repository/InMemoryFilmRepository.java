package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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

    private final Comparator<Film> likeComparator = (film1, film2) ->
            likes.get(film2.getId()).size() - likes.get(film1.getId()).size();

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film get(Long id) {
        return films.get(id);
    }

    public Film add(Film film) {
        log.trace("Текущий максимальный id фильма: {}", film.getId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен с id {}", film.getName(), film.getId());
        return film;
    }

    public Film update(Long id, Film film) {
        return films.put(id, film);
    }

}
