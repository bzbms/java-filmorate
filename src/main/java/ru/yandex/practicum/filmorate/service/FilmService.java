package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;

    public Collection<Film> getAll() {
        return filmRepository.getAll();
    }

    public Optional<Film> get(Long filmId) {
        return Optional.ofNullable(filmRepository.get(filmId));
    }

    public Film add(Film film) {
        film.setId(getNextId());
        return filmRepository.add(film);
    }

    public Optional<Film> update(Film film) {
        if (filmRepository.getFilms().containsKey(film.getId())) {
            Film existedFilm = filmRepository.getFilms().get(film.getId());
            if (film.getName() != null && !film.getName().isBlank()) {
                existedFilm.setName(film.getName());
            }
            if (film.getDescription() != null && !film.getDescription().isBlank()) {
                existedFilm.setDescription(film.getDescription());
            }
            if (film.getReleaseDate() != null) {
                existedFilm.setReleaseDate(film.getReleaseDate());
            }
            if (existedFilm.getDuration() != film.getDuration()) {
                existedFilm.setDuration(film.getDuration());
            }
            return Optional.ofNullable(filmRepository.update(existedFilm.getId(), existedFilm));
        }
        return Optional.empty();
    }

    public boolean addLike(Long filmId, Long userId) {
        return filmRepository.getLikes().get(filmId).add(userId);
    }

    public boolean deleteLike(Long filmId, Long userId) {
        return filmRepository.getLikes().get(filmId).remove(userId);
    }

    public Collection<Film> showPopularFilms(Integer count) {
        // Размер Set'а лайков по id одного фильма, сравниваем с размером Set'а лайков другого.
        // В начале списка фильмы с самым большим кол-вом лайков.
        return filmRepository.getFilms().values().stream()
                .sorted(filmRepository.getLikeComparator())
                .limit(count)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long uniqueId = filmRepository.getFilms().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }

}
