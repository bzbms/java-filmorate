package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Qualifier("JdbcGenreRepository")
@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = :id";
    private static final String GENRES_OF_FILM = """
            SELECT genres.id, genres.name
            FROM genres
            JOIN film_genre ON genres.id = film_genre.genre_id
            WHERE film_genre.film_id IN (:film_id)
            """;
    private static final String GENRES_IDS_OF_FILM = "SELECT genre_id FROM film_genre WHERE film_id = :film_id";

    @Override
    public Collection<Genre> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Genre> get(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper));
    }

    @Override
    public List<Integer> getGenresIdsOfFilm(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        return jdbc.queryForList(GENRES_IDS_OF_FILM, params, Integer.class);
    }

    @Override
    public List<Genre> getGenresOfFilm(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        return jdbc.query(GENRES_OF_FILM, params, mapper);
    }
}
