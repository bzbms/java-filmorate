package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    User add(User user);

    User update(User user);

    Optional<User> get(Long id);

    void addFriend(Long userId, Long friendId, boolean approved);

    void approveFriend(Long userId, Long friendId, boolean approved);

    void deleteFriend(Long userId, Long friendId);

    List<User> showFriendsByUser(Long userId);

    Collection<User> showFriendsCommonWithUser(Long userId, Long otherId);
}
