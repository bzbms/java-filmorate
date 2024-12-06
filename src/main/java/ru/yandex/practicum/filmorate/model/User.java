package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @NotNull(groups = {Update.class})
    long id;

    String name;

    @NotBlank(message = "Не указана почта", groups = {Create.class, Update.class})
    @Email(message = "Почта указана некорректно", groups = {Create.class, Update.class})
    String email;

    @Pattern(regexp = "^[A-Za-z0-9]+$",
            message = "login может содержать только цифры и символы латиницы",
            groups = {Create.class, Update.class})
    String login;

    @NotNull(message = "Не указана дата рождения", groups = {Create.class, Update.class})
    @Past(message = "Дата рождения должна быть указана в прошлом",
            groups = {Create.class, Update.class})
    LocalDate birthday;
}