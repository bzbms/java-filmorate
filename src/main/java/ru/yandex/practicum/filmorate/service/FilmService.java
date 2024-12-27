package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final Comparator<Film> likeComparator = (film1, film2) ->
            film2.getLikes().size() - film1.getLikes().size();

    public Collection<Film> getAll() {
        return filmRepository.getAll();
    }

    public Film add(Film film) {
        film.setId(filmRepository.getNextId());
        return filmRepository.add(film);
    }

    public Film update(Film film) {
        Film existedFilm = filmRepository.get(film.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", film.getId())));
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
        return filmRepository.update(existedFilm.getId(), existedFilm);
    }

    public Film get(Long filmId) {
        return filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
    }

    public void addLike(Long filmId, Long userId) {
        filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        if (filmRepository.getLikesAtFilm(filmId) == null) {
            filmRepository.setLikesAtFilm(filmId);
        } else if (filmRepository.getLikesAtFilm(filmId).contains(userId)) {
            throw new IncorrectRequestException(String.format("Лайк уже был добавлен фильму c id=%d от пользователя c id=%d", filmId, userId));
        } else {
            filmRepository.getLikesAtFilm(filmId).add(userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        if (filmRepository.getLikesAtFilm(filmId) == null) {
            throw new NotFoundException(String.format("У фильма %d нет лайков", filmId));
        }
        if (!filmRepository.getLikesAtFilm(filmId).contains(userId)) {
            throw new IncorrectRequestException(String
                    .format("Удалить лайк у фильму %d от пользователя %d не удалось - он не поставлен", filmId, userId));
        } else {
            filmRepository.getLikesAtFilm(filmId).remove(userId);
        }
    }

    public Collection<Film> showPopularFilms(Integer count) {
        // Размер Set'а лайков по id одного фильма, сравниваем с размером Set'а лайков другого.
        // В начале списка фильмы с самым большим кол-вом лайков.
        return filmRepository.getAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(likeComparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}
