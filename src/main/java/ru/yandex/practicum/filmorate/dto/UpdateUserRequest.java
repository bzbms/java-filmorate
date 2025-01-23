package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String name;
    private String login;
    private String email;
    private LocalDate birthday;

    public boolean hasUsername() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasLogin() {
        return ! (login == null || login.isBlank());
    }

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasBirthday() {
        return ! (birthday == null);
    }
}
