package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Create;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    @NotNull(groups = {Update.class})
    long id;

    @NotBlank(message = "Название не может быть пустым", groups = {Create.class, Update.class})
    String name;

    @NotBlank(message = "Описание не может быть пустым", groups = {Create.class, Update.class})
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов",
            groups = {Create.class, Update.class})
    String description;

    @NotNull(message = "Не указана дата релиза", groups = {Create.class, Update.class})
    @ReleaseDate(message = "Дата релиза раньше даты рождения кино", groups = {Create.class, Update.class})
    LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной", groups = {Create.class, Update.class})
    int duration;
}
