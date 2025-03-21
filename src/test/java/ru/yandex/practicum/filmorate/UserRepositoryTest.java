package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Import({JdbcUserRepository.class})
public class UserRepositoryTest {
    private final JdbcUserRepository userRepository;

    static User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("name1");
        user.setLogin("login1");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(2000, 1, 2));
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
        assertThat(userRepository.getAll()).hasSize(3);
    }

    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setName("name3");
        newUser.setLogin("login3");
        newUser.setEmail("ya3@ya.ru");
        newUser.setBirthday(LocalDate.of(2003, 1, 2));

        userRepository.add(newUser);
        assertThat(userRepository.getAll()).hasSize(3);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setName("newname");
        updatedUser.setLogin("newlogin");
        updatedUser.setEmail("newya@ya.ru");
        updatedUser.setBirthday(LocalDate.of(2001, 1, 2));

        userRepository.update(updatedUser);
        assertThat(userRepository.get(2L)).hasValue(updatedUser);
    }
}