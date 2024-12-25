package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public Optional<User> get(Long userId) {
        return Optional.ofNullable(repository.get(userId));
    }

    public User add(User user) {
        user.setId(getNextId());
        return repository.add(user);
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

    public boolean addFriend(Long userId, Long friendId) {
        if (userId == friendId) {
            return false;
        }
        if (repository.getFriendsByUser(userId) == null) {
            repository.setFriendsAtUser(userId, friendId);
        } else if (repository.getFriendsByUser(friendId) == null) {
            repository.setFriendsAtUser(friendId, userId);
            return true;
        } else {
            repository.addFriendsAtUser(userId, friendId);
            return repository.addFriendsAtUser(friendId, userId);
        }
        return false;
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        if (repository.getFriendsByUser(userId) == null || repository.getFriendsByUser(friendId) == null) {
            return false;
        } else {
            repository.removeFriendsAtUser(userId, friendId);
            return repository.removeFriendsAtUser(friendId, userId);
        }
    }

    public Collection<User> showFriendsByUser(Long userId) {
        Set<Long> friends = repository.getFriendsByUser(userId);
        Collection<User> users = new ArrayList<>();
        for (Long id : friends) {
            users.add(repository.get(id));
        }
        return users;//repository.getFriendsByUser(userId).stream().map(repository::get).collect(Collectors.toList());
    }

    public Collection<User> showFriendsCommonWithUser(Long userId, Long otherId) {
        Set<Long> intersection = new HashSet<>(repository.getFriendsByUser(userId));
        intersection.retainAll(repository.getFriendsByUser(otherId));
        return intersection.stream().map(id -> repository.get(id)).collect(Collectors.toList());
    }

    private long getNextId() {
        long uniqueId = repository.getUsers().keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++uniqueId;
    }

}
