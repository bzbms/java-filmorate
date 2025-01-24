package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.util.Collection;
import java.util.Optional;

@Qualifier("JdbcFilmRepository")
@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = :id";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, release_date, duration)" +
            "VALUES (:name, :description, :release_date, :duration)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = :name, description = :description, " +
            "release_date = :release_date, duration = :duration, rating_mpa_id = :rating_mpa_id WHERE id = :id";
    private static final String INSERT_LIKE = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id";
    private static final String SHOW_POPULAR_FILMS = """
            SELECT * FROM films
            LEFT OUTER JOIN likes ON films.id = likes.film_id
            GROUP BY films.id
            ORDER BY COUNT(likes.film_id) DESC
            LIMIT
            """;

    @Override
    public Collection<Film> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Film add(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        //params.addValue("rating_mpa_id", film.getRatingMpaId());

        jdbc.update(INSERT_QUERY, params, keyHolder, new String[]{"id"});

        film.setId(keyHolder.getKeyAs(Integer.class).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", film.getId());
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("rating_mpa_id", film.getRatingMpaId());
        jdbc.update(UPDATE_QUERY, params);

        return film;
    }

    @Override
    public Optional<Film> get(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);
        jdbc.update(INSERT_LIKE, params);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);
        jdbc.update(DELETE_LIKE, params);
    }

    @Override
    public Collection<Film> showPopularFilms(Integer count) {
        return jdbc.query(SHOW_POPULAR_FILMS + count, mapper);
    }

}
