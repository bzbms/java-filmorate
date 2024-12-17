package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Group;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmRepository repository;
    private final FilmService service;

    @GetMapping
    public Collection<Film> showAll() {
        log.debug("Запрошен список всех фильмов.");
        return repository.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Group.Create.class) @RequestBody Film film) {
        log.debug("Запрос на создание фильма {} прошёл валидацию.", film.getName());
        return repository.add(film);
    }

    @PutMapping
    public Film update(@Validated(Group.Update.class) @RequestBody Film film) {
        log.debug("Запрос на обновление фильма {} (id={}) прошёл валидацию.", film.getName(), film.getId());
        return repository.update(film);
    }



    @GetMapping("/post/{postId}")
    public Post findPost(@PathVariable("postId") Long postId){
        return postService.findPostById(postId);
    }
    public Post findPostById(Long postId) {
        return posts.values().stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пост № %d не найден", postId)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IncorrectCountException e) {
        return new ErrorResponse(
                "Ошибка с параметром count." + e.getMessage()
        );
    }
/*
    @PathVariable добавьте возможность получать каждый фильм и данные о пользователях по их уникальному идентификатору:
    GET .../users/{id}
    GET .../films/{id}

    PUT /users/{id}/friends/{friendId} — добавление в друзья.
    DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.

    PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     Если значение параметра count не задано, верните первые 10.

    Убедитесь, что ваше приложение возвращает корректные HTTP-коды:
        400 — если ошибка валидации: ValidationException;
        404 — для всех ситуаций, если искомый объект не найден;
        500 — если возникло исключение.

    Настройте ExceptionHandler для централизованной обработки ошибок.*/

}