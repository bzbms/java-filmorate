package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    public Collection<Film> getAll() {
        return filmRepository.getAll();
    }

    public Film add(Film film) {
        mpaRepository.get(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг фильма(%d) c id=%d не найден", film.getId(), film.getMpa().getId())));
        List<Genre> genresOfFilm = film.getGenres();
        genresOfFilm.forEach(genre -> genreRepository.get(genre.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Жанр фильма(%d) c id=%d не найден", film.getId(), genre.getId()))));

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
        if (existedFilm.getMpa() != film.getMpa()) {
            existedFilm.setMpa(film.getMpa());
        }
        if (existedFilm.getGenres() != film.getGenres()) {
            existedFilm.setGenres(film.getGenres());
        }
        return filmRepository.update(existedFilm);
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
/*        if (film.getLikes().contains(userId)) {
            throw new IncorrectRequestException(String.format("Лайк уже был добавлен фильму c id=%d от пользователя c id=%d", filmId, userId));
        } else {
            film.getLikes().add(userId);
        }*/
        filmRepository.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
/*        if (!film.getLikes().contains(userId)) {
            throw new IncorrectRequestException(String
                    .format("Удалить лайк у фильму %d от пользователя %d не удалось - он не поставлен", filmId, userId));
        } else {
            film.getLikes().remove(userId);
        }*/
        filmRepository.removeLike(filmId, userId);
    }

    public Collection<Film> showPopularFilms(Integer count) {
        if (count < 0) {
            throw new IncorrectRequestException(String.format("Кол-во фильмов не должно быть отрицательным, count: %d", count));
        }
        return filmRepository.showPopularFilms(count);
    }
}
