package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    Collection<Film> getAll();

    Film add(Film film);

    Film update(Film film);

    Optional<Film> get(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Collection<Film> showPopularFilms(Integer count);

}
