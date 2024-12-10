package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.Group;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void badEmail() {
        User user = new User(1, "name", "emailya.ru@", "login", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Group.Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badLogin() {
        User user = new User(1, "name", "email@ya.ru", "Ы", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Group.Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Pattern.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badBirthday() {
        User user = new User(1, "name", "email@ya.ru", "login", LocalDate.of(2990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Group.Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Past.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void nullLogin() {
        User user = new User();
        user.setEmail("email@ya.ru");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Group.Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void nullBirthday() {
        User user = new User();
        user.setLogin("login");
        user.setEmail("email@ya.ru");

        Set<ConstraintViolation<User>> violations = validator.validate(user, Group.Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

}
