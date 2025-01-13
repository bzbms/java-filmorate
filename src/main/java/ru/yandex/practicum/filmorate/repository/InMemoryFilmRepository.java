package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
    private int uniqueId = 0;

    public Collection<Film> getAll() {
        return films.values();
    }

    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Long id, Film film) {
        return films.put(id, film);
    }

    public Optional<Film> get(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    public Set<Long> getLikesAtFilm(Long id) {
        return films.get(id).getLikes();
    }

    public void addLikesAtFilm(Long id) {
        films.get(id).getLikes().add(id);
    }

    public void removeLikesAtFilm(Long id) {
        films.get(id).getLikes().remove(id);
    }

    public long getNextId() {
        return ++uniqueId;
    }

}
