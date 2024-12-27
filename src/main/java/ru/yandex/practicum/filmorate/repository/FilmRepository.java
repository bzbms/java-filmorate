package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository {

    Collection<Film> getAll();

    Film add(Film film);

    Film update(Long id, Film film);

    Optional<Film> get(Long id);

    void setLikesAtFilm(Long userId);

    Set<Long> getLikesAtFilm(Long id);

    long getNextId();

}
