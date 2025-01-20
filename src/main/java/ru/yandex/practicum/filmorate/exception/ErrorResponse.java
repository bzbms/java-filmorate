package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String error) {
        super(error);
    }
}