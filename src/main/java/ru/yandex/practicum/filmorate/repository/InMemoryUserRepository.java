package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@Getter
@Setter
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final HashMap<Long, Set<Long>> friends = new HashMap<>();

    public Collection<User> getAll() {
        return users.values();
    }

    public User get(Long id) {
        return users.get(id);
    }

    public HashMap<Long, Set<Long>> getFriends() {
        return friends;
    }

    public Set<Long> getFriendsByUser(Long id) {
        return friends.get(id);
    }

    public boolean setFriendsAtUser(Long userId, Long otherId) {
        return friends.get(userId).add(otherId);
    }

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

}
