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

    Set<Long> getLikesAtFilm(Long id);

    void addLikesAtFilm(Long userId);

    void removeLikesAtFilm(Long userId);

    long getNextId();

}
