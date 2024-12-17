package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository repository;

    public void addLike(Long id) {
        Film film = repository.get(id);
        if (film.getLikes().add(id)) {
            log.info("Лайк добавлен фильму {}", film.getName());
            repository.update(film);
        } else {
            log.info("У фильма {} лайк уже был поставлен", film.getName());
        }
    }

    public void deleteLike(Long id) {
        Film film = repository.get(id);
        if (film.getLikes().remove(id)) {
            log.info("Лайк удалён у фильма {}", film.getName());
            repository.update(film);
        } else {
            log.debug("У фильма {} не было лайка от пользователя с id={}", film.getName(), id);
        }
    }

    /*
        вывод 10 наиболее популярных фильмов по количеству лайков.
       */
    public void getMostPopularFilms() {
        Collection<Film> films = repository.getAll();
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
