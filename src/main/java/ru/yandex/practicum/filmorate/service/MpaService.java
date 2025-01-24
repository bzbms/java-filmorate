package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository repository;

    public Collection<Mpa> getAll() {
        return repository.getAll();
    }

    public Mpa get(Integer id) {
        return repository.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг MPA c id=%d не найден", id)));
    }

}
