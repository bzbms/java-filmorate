package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Group;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @NotNull(groups = {Group.Update.class})
    private long id;

    @NotBlank(message = "Название не может быть пустым", groups = {Group.Create.class})
    private String name;

    @NotBlank(message = "Описание не может быть пустым", groups = {Group.Create.class})
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов",
            groups = {Group.Create.class, Group.Update.class})
    private String description;

    @ReleaseDate(message = "Дата релиза раньше даты рождения кино", groups = {Group.Create.class, Group.Update.class})
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной", groups = {Group.Create.class, Group.Update.class})
    private int duration;

    private Set<Long> likes = new HashSet<>();

    private Mpa mpa;

    private TreeSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
}
