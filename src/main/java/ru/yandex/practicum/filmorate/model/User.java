package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Group;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = {Group.Update.class})
    private long id;

    private String name;

    @NotNull(message = "Не указан логин", groups = {Group.Create.class})
    @Pattern(regexp = "^[A-Za-z0-9]+$",
            message = "login может содержать только цифры и символы латиницы",
            groups = {Group.Create.class, Group.Update.class})
    private String login;

    @Email(message = "Почта указана некорректно", groups = {Group.Create.class, Group.Update.class})
    private String email;

    @NotNull(message = "Не указана дата рождения", groups = {Group.Create.class})
    @Past(message = "Дата рождения должна быть указана в прошлом",
            groups = {Group.Create.class, Group.Update.class})
    private LocalDate birthday;
}