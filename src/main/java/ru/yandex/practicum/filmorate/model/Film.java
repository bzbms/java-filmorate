package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Group;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @NotNull(groups = {Group.Update.class})
    private long id;

    @NotEmpty(message = "Не указано название для обновления", groups = {Group.Update.class})
    @NotBlank(message = "Название не может быть пустым", groups = {Group.Create.class})
    private String name;

    @NotEmpty(message = "Не указано описание для обновления", groups = {Group.Update.class})
    @NotBlank(message = "Описание не может быть пустым", groups = {Group.Create.class})
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов",
            groups = {Group.Create.class, Group.Update.class})
    private String description;

    @NotNull(message = "Не указана дата релиза", groups = {Group.Create.class, Group.Update.class})
    @ReleaseDate(message = "Дата релиза раньше даты рождения кино", groups = {Group.Create.class, Group.Update.class})
    private LocalDate releaseDate;

    @NotNull(groups = {Group.Update.class})
    @Positive(message = "Продолжительность должна быть положительной", groups = {Group.Create.class, Group.Update.class})
    private int duration;
}
