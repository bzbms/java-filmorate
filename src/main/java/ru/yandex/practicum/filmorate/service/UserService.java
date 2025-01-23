package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
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
    private final UserRepository repository;

    public Collection<User> getAll() {
        return repository.getAll();
    }

    public UserDto add(NewUserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getName().isBlank()) {
            userRequest.setName(userRequest.getLogin());
        }
        User user = UserMapper.mapToUser(userRequest);
        user = repository.add(user);

        return UserMapper.mapToUserDto(user);
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
        User user = repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        User friend = repository.get(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        User friend = repository.get(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь-друг c id=%d не найден", friendId)));
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> showFriendsByUser(Long userId) {
        User user = repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        return user.getFriends().stream()
                .map(id -> repository.get(id).get())
                .collect(Collectors.toList());
    }

    public Collection<User> showFriendsCommonWithUser(Long userId, Long otherId) {
        User user = repository.get(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", userId)));
        User otherUser = repository.get(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id=%d не найден", otherId)));
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());
        return intersection.stream()
                .map(id -> repository.get(id).get())
                .collect(Collectors.toList());
    }
}
