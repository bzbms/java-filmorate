package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.TreeSet;

@Qualifier("JdbcFilmRepository")
@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = :id JOIN rating_mpa ON films.rating_mpa_id = rating_mpa.if WHERE id = :id";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, release_date, duration, rating_mpa_id)" +
            "VALUES (:name, :description, :release_date, :duration, :rating_mpa_id)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = :name, description = :description, " +
            "release_date = :release_date, duration = :duration, rating_mpa_id = :rating_mpa_id WHERE id = :id";
    private static final String INSERT_LIKE = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id";
    private static final String SHOW_POPULAR_FILMS = """
            SELECT * FROM films
            WHERE id IN (
            SELECT film_id
            FROM likes
            GROUP BY film_id
            ORDER BY count(user_id) DESC
            )
            LIMIT
            """;
    private static final String INSERT_GENRES = "INSERT INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)";
    private static final String CLEAN_GENRES = "DELETE FROM film_genre WHERE film_id = :film_id";

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
        params.addValue("rating_mpa_id", film.getMpa().getId());

        jdbc.update(INSERT_QUERY, params, keyHolder, new String[]{"id"});
        film.setId(keyHolder.getKeyAs(Integer.class).longValue());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            operateGenres(film.getId(), film.getGenres());
        }
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
        params.addValue("rating_mpa_id", film.getMpa().getId());
        jdbc.update(UPDATE_QUERY, params);

        if (film.getGenres() != null) {
            MapSqlParameterSource genresCleanParams = new MapSqlParameterSource();
            genresCleanParams.addValue("film_id", film.getId());
            jdbc.update(CLEAN_GENRES, genresCleanParams);

            operateGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Optional<Film> get(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = :id "+
                "JOIN rating_mpa.name ON films.rating_mpa_id = rating_mpa.id";

        SqlRowSet results = jdbc.queryForRowSet(FIND_BY_ID_QUERY, params);
        Film film = mapFilm(results);
        film.getMpa().setName(JdbcMpaRepository.getMpaName(film.getMpa().getId()));

        film.setGenres(new TreeSet<>(genreRepository.getGenresOfFilm(film.getId())));
        film.getLikes().addAll(userRepository.getUserLikes(film.getId()));

        return Optional.of(film);
    }

    static Film mapFilm(SqlRowSet srs) {
        Film film = new Film();
        film.setId(srs.getLong("id"));
        film.setName(srs.getString("name"));
        film.setDescription(srs.getString("description"));
        film.setReleaseDate(srs.getDate("release_date").toLocalDate());
        film.setDuration(srs.getInt("duration"));
        film.getMpa().setId(srs.getInt("rating_mpa_id"));
        film.getMpa().setName(srs.getString("rating_mpa.name"));
        return film;
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
        return jdbc.query(SHOW_POPULAR_FILMS + " " + count, mapper);
    }

    private void operateGenres(Long filmId, TreeSet<Genre> genresToSave) {
        SqlParameterSource[] genresBatch = new MapSqlParameterSource[genresToSave.size()];
        MapSqlParameterSource genresParams;

        for (int i = 0; i < genresBatch.length; i++) {
            genresParams = new MapSqlParameterSource();
            genresParams.addValue("film_id", filmId);
            genresParams.addValue("genre_id", genresToSave.getFirst());
            genresBatch[i] = genresParams;
            genresToSave.removeFirst();
        }

        jdbc.batchUpdate(INSERT_GENRES, genresBatch);
/*
        genresToSave.stream().forEachOrdered(genre -> {
            genresParams.addValue("film_id", filmId);
            genresParams.addValue("genre_id", genre.getId());
            jdbc.update(INSERT_GENRES, genresParams);
        });*/
    }

}
