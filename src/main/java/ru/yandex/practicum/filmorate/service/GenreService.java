package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository repository;

    public Collection<Genre> getAll() {
        return repository.getAll();
    }

    public Genre get(Integer id) {
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр c id=%d не найден", id)));
    }

}