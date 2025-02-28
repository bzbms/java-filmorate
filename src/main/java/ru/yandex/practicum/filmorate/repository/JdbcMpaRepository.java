package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Qualifier("JdbcMpaRepository")
@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE id = :id";
    private static final String FIND_NAME_BY_ID_QUERY = "SELECT name FROM rating_mpa WHERE id = :id";

    @Override
    public Collection<Mpa> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Mpa> get(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper));
    }
/*
    static String getMpaName(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbc.queryForObject(FIND_NAME_BY_ID_QUERY, params, (rs, rowNum) -> rs.getString("name"));
    }*/

    static String getMpaName(Integer id) {
        Film film = new Film();
        film.setId(srs.getLong("id"));
        film.setName(srs.getString("name"));
        film.setDescription(srs.getString("description"));
        film.setReleaseDate(srs.getDate("release_date").toLocalDate());
        film.setDuration(srs.getInt("duration"));
        film.getMpa().setId(srs.getInt("rating_mpa_id"));
        return film;
    }

}