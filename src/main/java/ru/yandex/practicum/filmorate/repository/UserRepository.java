package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User add(User user);

    User update(User user);

    Collection<User> getAll();

    Optional<User> getUser(Long id);
}
