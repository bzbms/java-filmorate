package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmRepository {

    Film add(Film film);

    Film update(Long id, Film film);

    Collection<Film> getAll();

    Film get(Long id);

    Map<Long, Film> getFilms();

    Map<Long, Set<Long>> getLikes();

    Comparator<Film> getLikeComparator();
}
