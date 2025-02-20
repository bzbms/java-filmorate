package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class})
public class UserRepositoryTest {
    private final JdbcUserRepository userRepository;

    static User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("name1");
        user.setLogin("login1");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(2000,1,2));
        return user;
    }

    @Test
    public void testGetUserById() {
        Optional<User> userOptional = userRepository.get(1L);

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestUser());
    }

    @Test
    public void testGetAllUsers() {
        assertThat(userRepository.getAll()).hasSize(2);
    }

    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setName("name3");
        newUser.setLogin("login3");
        newUser.setEmail("ya3@ya.ru");
        newUser.setBirthday(LocalDate.of(2003,1,2));

        userRepository.add(newUser);
        assertThat(userRepository.getAll()).hasSize(3);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setName("newname1");
        updatedUser.setLogin("login1");
        updatedUser.setEmail("ya@ya.ru");
        updatedUser.setBirthday(LocalDate.of(2000,1,2));

        userRepository.update(updatedUser);
        assertThat(userRepository.get(3L)).;
    }
}
