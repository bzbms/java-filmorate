package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

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
 Set<Film> films = new HashSet<>();
 while (films.size() < count) {
     films.add(filmRepository.getSortedFilms().stream().iterator().next().)
 }
        filmRepository.getSortedFilms().stream().
        return filmRepository.getSortedFilms();
/*        Collection<Film> films = filmRepository.getAll();
        int size = 0;
        for (Film film : films) {
            if (size < film.getLikes().size()) {
                size = film.getLikes().size();
            }
        }

        films.stream().sorted().peek(film -> film.getLikes().size()).takeWhile()*/
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
