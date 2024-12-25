package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
@Getter
@Setter
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final HashMap<Long, Set<Long>> friends = new HashMap<>();

    public User add(User user) {
        log.trace("Текущий максимальный id пользователя: {}", user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя с id {} было присвоено от login.", user.getId());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен с id {}", user.getLogin(), user.getId());
        return user;
    }

    public User update(Long id, User user) {
        return users.put(id, user);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User get(Long id) {
        return users.get(id);
    }

    public Set<Long> getFriendsByUser(Long id) {
        return friends.get(id);
    }

    public void setFriendsAtUser(Long userId, Long otherId) {
        Set<Long> friendsIds = new HashSet<>();
        friendsIds.add(otherId);
        friends.put(userId, friendsIds);
    }

    public boolean addFriendsAtUser(Long userId, Long otherId) {
        return friends.get(userId).add(otherId);
    }

    public boolean removeFriendsAtUser(Long userId, Long otherId) {
        return friends.get(userId).remove(otherId);
    }

}
