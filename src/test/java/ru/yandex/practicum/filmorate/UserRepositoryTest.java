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
    public void testFindUserById() {

        Optional<User> userOptional = userRepository.get(1L);

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestUser());
    }
/*
    - список всех
            - получение юзера
            - добавление юзера
            - обновление юзера
-*/
}
