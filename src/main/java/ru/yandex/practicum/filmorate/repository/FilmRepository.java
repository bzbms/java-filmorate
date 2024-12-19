package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository {

    Film add(Film film);

    Film update(Film film);

    Collection<Film> getAll();

    Optional<Film> getFilm(Long id);

    HashMap<Long, Set<Long>> getLikes();

}
