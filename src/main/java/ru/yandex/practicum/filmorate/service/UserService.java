package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    UserRepository repository;
    Set<Long> friends;

    public Optional<User> get(Long userId) {
        return Optional.ofNullable(repository.get(userId));
    }

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public User add(User user) {
        user.setId(getNextId());
        return repository.add(user);
    }
/*
    будет отвечать за такие операции с пользователями, как
    добавление в друзья,
    удаление из друзей,
    вывод списка общих друзей.

    Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
    То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.*/

    private long getNextId() {
        long uniqueId = repository.getUsers().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }

    public Optional<User> update(User user) {
        if (repository.getUsers().containsKey(user.getId())) {
            User existedUser = repository.getUsers().get(user.getId());
            if (user.getLogin() != null && !user.getLogin().isBlank()) {
                existedUser.setLogin(user.getLogin());
            }
            if (user.getEmail() != null) {
                existedUser.setEmail(user.getEmail());
            }
            if (user.getBirthday() != null) {
                existedUser.setBirthday(user.getBirthday());
            }
            if (user.getName() == null || user.getName().isBlank()) {
                existedUser.setName(existedUser.getLogin());
            } else if (!existedUser.getName().equals(user.getName())) {
                existedUser.setName(user.getName());
            }
            return Optional.ofNullable(repository.update(existedUser.getId(), existedUser));
        }
        return Optional.empty();
    }

    public void addFriend(Long userId, Long userId1) {
    }

    public void deleteFriend(Long userId, Long userId1) {
    }

    public Collection<User> showFriendsByUser(Long userId) {
    }
}
