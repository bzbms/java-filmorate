package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private int uniqueId = 0;

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
        System.out.println(users);
        return Optional.ofNullable(users.get(id));
    }

    public Set<Long> getFriendsByUser(Long id) {
        return users.get(id).getFriends();
    }

    public void addFriendsAtUser(Long userId, Long otherId) {
        users.get(userId).getFriends().add(otherId);
    }

    public void removeFriendsAtUser(Long userId, Long otherId) {
        users.get(userId).getFriends().remove(otherId);

    }

    public long getNextId() {
        return ++uniqueId;
    }

}
