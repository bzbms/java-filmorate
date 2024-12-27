package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long id, User user) {
        return users.put(id, user);
    }

    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Set<Long> getFriendsByUser(Long id) {
        return users.get(id).getFriends();
    }

    public void setFriendsAtUser(Long userId) {
        users.get(userId).setFriends(new HashSet<>());
    }

    public void addFriendsAtUser(Long userId, Long otherId) {
        users.get(userId).getFriends().add(otherId);
    }

    public void removeFriendsAtUser(Long userId, Long otherId) {
        users.get(userId).getFriends().remove(otherId);
    }

    public long getNextId() {
        return users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(1);
    }

}
