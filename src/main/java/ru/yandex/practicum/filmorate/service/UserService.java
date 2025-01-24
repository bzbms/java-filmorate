package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return repository.add(user);
    }

    public User update(User user) {
        User existedUser = repository.get(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", user.getId())));
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
        return repository.update(existedUser);
    }

    public User get(Long userId) {
        return repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
    }

    public void addFriend(Long userId, Long friendId) {
        existChecker(userId);
        existChecker(friendId);
        repository.addFriend(userId, friendId, true);
        repository.addFriend(friendId, userId, false);
    }

    public void approveFriend(Long userId, Long friendId) {
        existChecker(userId);
        existChecker(friendId);
        repository.approveFriend(userId, friendId, true);
    }

    public void deleteFriend(Long userId, Long friendId) {
        existChecker(userId);
        existChecker(friendId);
        repository.deleteFriend(userId, friendId);
    }

    public Collection<User> showFriendsByUser(Long userId) {
        existChecker(userId);
        return repository.showFriendsByUser(userId);
    }

    public Collection<User> showFriendsCommonWithUser(Long userId, Long otherId) {
        existChecker(userId);
        existChecker(otherId);
        return repository.showFriendsCommonWithUser(userId, otherId);
    }

    private void existChecker(Long userId) {
        repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
    }
}
