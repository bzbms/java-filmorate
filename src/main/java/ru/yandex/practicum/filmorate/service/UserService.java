package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    Set<Long> friends;
/*
    будет отвечать за такие операции с пользователями, как
    добавление в друзья,
    удаление из друзей,
    вывод списка общих друзей.

    Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
    То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.*/
}
