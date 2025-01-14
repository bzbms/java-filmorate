package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    User add(User user);

    User update(Long id, User user);

    Optional<User> get(Long id);

}
