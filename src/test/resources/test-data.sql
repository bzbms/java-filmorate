MERGE INTO genres (id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

MERGE INTO rating_mpa (id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

INSERT INTO USERS (name, login, email, birthday) VALUES
('name1','login1', 'ya@ya.ru', '2000-01-02'),
('name2', 'login2', 'ty@ty.ru', '2002-02-10');

INSERT INTO FILMS (name, description, release_date, duration, rating_mpa_id) VALUES
('123', '111111', '2022-12-02', 60, 1),
('321', '222222', '2011-11-11', 30, 2);

INSERT INTO FILM_GENRE (film_id, genre_id) VALUES
('1', '2'),
('2', '4');