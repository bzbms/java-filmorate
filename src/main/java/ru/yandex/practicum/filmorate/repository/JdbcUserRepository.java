package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Qualifier("JdbcUserRepository")
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = :id";
    private static final String INSERT_QUERY = "INSERT INTO users (name, login, email, birthday)" +
            "VALUES (:name, :login, :email, :birthday)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = :name, login = :login, " +
            "email = :email, birthday = :birthday WHERE id = :id";
    private static final String INSERT_FRIEND = "INSERT INTO friends (user_id, friend_id, approved) VALUES (:user_id, :friend_id, :approved)";
    private static final String FRIEND_CONFIRMATION = "UPDATE friends SET approved = :approved WHERE user_id = :user_id AND friend_id = :friend_id";
    private static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";
    private static final String FIND_FRIENDS_QUERY = """
            SELECT * FROM users
            JOIN friends ON users.id = friends.user_id
            WHERE friends.user_id = :id
            """;
    private static final String FIND_COMMON_FRIENDS = """
            SELECT * FROM users
            WHERE id IN (
            SELECT friend_id FROM friends
            WHERE user_id = :user_id
            INTERSECT
            SELECT friend_id from friends
            WHERE user_id = :friend_id)
            """;
    private static final String USER_LIKES_QUERY = "SELECT user_id FROM likes WHERE film_id = :film_id";

    @Override
    public User add(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());

        jdbc.update(INSERT_QUERY, params, keyHolder, new String[]{"id"});

        user.setId(keyHolder.getKeyAs(Integer.class).longValue());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(UPDATE_QUERY, params);

        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper));
    }

    @Override
    public void addFriend(Long userId, Long friendId, boolean approved) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", friendId);
        params.addValue("approved", approved);
        jdbc.update(INSERT_FRIEND, params);
    }

    @Override
    public void approveFriend(Long userId, Long friendId, boolean approved) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", friendId);
        params.addValue("approved", approved);
        jdbc.update(FRIEND_CONFIRMATION, params);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", friendId);
        jdbc.update(DELETE_FRIEND, params);
    }

    @Override
    public List<User> showFriendsByUser(Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", userId);
        return jdbc.query(FIND_FRIENDS_QUERY, params, mapper);
    }

    @Override
    public Collection<User> showFriendsCommonWithUser(Long userId, Long otherId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", otherId);
        return jdbc.query(FIND_COMMON_FRIENDS, params, mapper);
    }

    @Override
    public Collection<Long> getUserLikes(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        return jdbc.queryForList(USER_LIKES_QUERY, params, Long.class);
    }

}
