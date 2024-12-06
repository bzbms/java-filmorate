package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void badName() {
        Film film = new Film(1, "", "description", LocalDate.of(1990, 1, 1), 10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badReleaseDate() {
        Film film = new Film(1, "name", "description", LocalDate.of(1790, 1, 1), 10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(ReleaseDate.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badDescription() {
        Film film = new Film(1, "name", "", LocalDate.of(1990, 1, 1), 10);
        film.setDescription("Плюсы и минусы видеорегистраторов:\n" +
                "На Techage легче попасть. Множество добавлений в приложение. Никак поменять положение времени\\названия канала, на старых версиях и название канала не поменять.\n" +
                "У Praxis умирает облако. Множество добавлений в приложение.\n" +
                "У HiWatch слишком много паролей и привязки. Зато недавно сделали нормальную детекцию в приложении.\n" +
                "Tantos - понятный интерфейс, квоты для камер. Множество добавлений в приложение. Проблемная детекция движения\n" +
                "У ON есть qr-код на корпусе. Только одна привязка к приложению. Проблемная детекция движения");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    @Test
    void badDuration() {
        Film film = new Film(1, "name", "description", LocalDate.of(1990, 1, 1), -10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), "Список нарушений валидации не пуст.");
        ConstraintViolation<Film> violation = violations.stream().findFirst().get();
        assertEquals(Positive.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

}