package ru.yandex.practicum.filmorate.repository.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final MpaRepository mpaRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaRepository.get(rs.getInt("rating_mpa_id")).get());
 /*       film.getLikes().addAll(userRepository.getUserLikes(film.getId()));
        film.setGenres(List.copyOf(genreRepository.getGenresOfFilm(film.getId())));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);*/

        film.getLikes().addAll(userRepository.getUserLikes(film.getId()));
        Set<Long> likes = new HashSet<>();
        likes.add(1L);
        film.setLikes(likes);

        List<Genre> genres = new ArrayList<>();
        Genre gen = new Genre();
        gen.setId(1);
        gen.setName("121");
        genres.add(gen);
        film.setGenres(genres);
        return film;
    }
}