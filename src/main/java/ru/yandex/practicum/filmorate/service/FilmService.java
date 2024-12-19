package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public void addLike(Long filmId, Long userId) {
        filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм c id=%d не найден", filmId)));
        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", filmId)));
        filmRepository.getLikes().get(filmId).add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmRepository.getLikes().get(filmId).remove(userId);
    }

    /*
        вывод 10 наиболее популярных фильмов по количеству лайков. должно быть логикой хранилища(в будущей бд)
       */
    public void showPopularFilms() {
        Collection<Film> films = filmRepository.getAll();
        int size = 0;
        for (Film film : films) {
            if (size < film.getLikes().size()) {
                size = film.getLikes().size();
            }
        }

        films.stream().sorted().peek(film -> film.getLikes().size()).takeWhile()
        log.info("Лайк добавлен фильму {}", film.getName());
    }
}
