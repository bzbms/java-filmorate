package ru.yandex.practicum.filmorate.exception;

class ErrorResponse extends RuntimeException {
    public ErrorResponse(String message) {
        super(message);
    }
}
