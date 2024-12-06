package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.Create;

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

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badLogin() {
        User user = new User(1, "name", "email@ya.ru", "Ы", LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Pattern.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badBirthday() {
        User user = new User(1, "name", "email@ya.ru", "login", LocalDate.of(2990, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<User> violation = violations.stream().findFirst().get();
        assertEquals(Past.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

}
