package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Collection<Genre> getAll();

    Optional<Genre> get(Integer id);

    List<Integer> getGenresIdsOfFilm(Long id);

    List<Genre> getGenresOfFilm(Long filmId);
}
