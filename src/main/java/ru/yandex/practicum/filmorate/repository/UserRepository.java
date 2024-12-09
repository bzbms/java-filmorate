package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    public User add(User user) {
        user.setId(getNextId());
        log.trace("Текущий максимальный id пользователя: {}", user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя с id {} было присвоено от login.", user.getId());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен с id {}", user.getLogin(), user.getId());
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            User existedUser;
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.debug("Имя пользователя с id {} было присвоено от login.", user.getId());
            }
            existedUser = user;
            users.put(existedUser.getId(), existedUser);
            log.info("Пользователь {} с id {} обновлён", user.getLogin(), user.getId());
            return existedUser;
        }
        log.debug("Запрошен пользователь с неизвестным id = {}", user.getId());
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден.");
    }

    public Collection<User> getAll() {
        return users.values();
    }

    private long getNextId() {
        long uniqueId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }
}
