package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    UserRepository repository;

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

    public boolean addFriend(Long userId, Long otherId) {
        repository.setFriendsAtUser(userId, otherId);
        return repository.setFriendsAtUser(otherId, userId);
    }

    public boolean deleteFriend(Long userId, Long otherId) {
        repository.removeFriendsAtUser(userId, otherId);
        return repository.removeFriendsAtUser(otherId, userId);
    }

    public Collection<User> showFriendsByUser(Long userId) {
        return repository.getFriendsByUser(userId).stream().map(id -> repository.get(id)).collect(Collectors.toList());
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
