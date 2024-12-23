package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserRepository {

    User add(User user);

    User update(Long id, User user);

    Collection<User> getAll();

    User get(Long id);

    Map<Long, User> getUsers();
}
