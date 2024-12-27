package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public User add(User user) {
        user.setId(repository.getNextId());
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
        return repository.update(existedUser.getId(), existedUser);
    }

    public User get(Long userId) {
        return repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
    }

    public void addFriend(Long userId, Long friendId) {
        repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        repository.get(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        if (repository.getFriendsByUser(userId) == null) {
            repository.setFriendsAtUser(userId);
            repository.addFriendsAtUser(userId, friendId);
        } else if (repository.getFriendsByUser(friendId) == null) {
            repository.setFriendsAtUser(friendId);
            repository.addFriendsAtUser(friendId, userId);
        } else {
            repository.addFriendsAtUser(userId, friendId);
            repository.addFriendsAtUser(friendId, userId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        if (!userId.equals(friendId)) {
            repository.get(friendId)
                    .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        }
        if (repository.getFriendsByUser(userId) == null) {
            throw new NotFoundException(String.format("У пользователя %d нет друзей", userId));
        } else if (repository.getFriendsByUser(userId).contains(friendId)) {
            repository.removeFriendsAtUser(userId, friendId);
            repository.removeFriendsAtUser(friendId, userId);
        }
    }

    public Collection<User> showFriendsByUser(Long userId) {
        repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        if (repository.getFriendsByUser(userId) == null) {
            repository.setFriendsAtUser(userId);
        }
        return repository.getFriendsByUser(userId).stream()
                .map(id -> repository.get(id).get())
                .collect(Collectors.toList());
    }

    public Collection<User> showFriendsCommonWithUser(Long userId, Long otherId) {
        repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        repository.get(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", otherId)));
        if (repository.getFriendsByUser(userId) == null) {
            repository.setFriendsAtUser(userId);
        } else if (repository.getFriendsByUser(otherId) == null) {
            repository.setFriendsAtUser(otherId);
        }
        Set<Long> intersection = new HashSet<>(repository.getFriendsByUser(userId));
        intersection.retainAll(repository.getFriendsByUser(otherId));
        return intersection.stream()
                .map(id -> repository.get(id).get())
                .collect(Collectors.toList());

    }

}
