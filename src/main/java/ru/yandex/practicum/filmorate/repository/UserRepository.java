package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Collection<User> getAll();

    User add(User user);

    User update(Long id, User user);

    Optional<User> get(Long id);

    Set<Long> getFriendsByUser(Long id);

    void setFriendsAtUser(Long userId);

    void addFriendsAtUser(Long userId, Long otherId);

    void removeFriendsAtUser(Long userId, Long otherId);

    long getNextId();

}
