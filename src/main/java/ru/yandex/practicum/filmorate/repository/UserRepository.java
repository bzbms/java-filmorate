package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {

    User add(User user);

    User update(User user);

    Collection<User> getAll();
}
