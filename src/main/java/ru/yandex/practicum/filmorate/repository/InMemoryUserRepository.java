package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private int uniqueId = 0;

    public Collection<User> getAll() {
        return users.values();
    }

    public User add(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long id, User user) {
        return users.put(id, user);
    }

    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long getNextId() {
        return ++uniqueId;
    }

}
