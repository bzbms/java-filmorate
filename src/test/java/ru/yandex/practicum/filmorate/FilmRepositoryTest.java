package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcFilmRepository.class})
public class FilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;

    static Film getTestFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("123");
        film.setDescription("111111");
        film.setReleaseDate(LocalDate.of(2022, 12, 2));
        film.setDuration(60);

        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");
        film.setMpa(mpa);

        Genre genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("Комедия");
        Genre genre2 = new Genre();
        genre2.setId(2);
        genre2.setName("Драма");
        Set<Genre> genres = Set.of(genre1, genre2);
        film.setGenres(genres);

        Set<Long> likes = Set.of(1L, 2L);
        film.setLikes(likes);

        return film;
    }

    @Test
    public void testGetFilmById() {
        Optional<Film> filmOptional = filmRepository.get(1L);

        assertThat(filmOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm());
    }

    @Test
    public void testGetAllFilms() {
        assertThat(filmRepository.getAll()).hasSize(2);
    }

    @Test
    public void testAddFilm() {
        Film newFilm = new Film();
        newFilm.setName("333");
        newFilm.setDescription("3");
        newFilm.setReleaseDate(LocalDate.of(2023, 3, 3));
        newFilm.setDuration(30);

        Mpa mpa = new Mpa();
        mpa.setId(3);
        mpa.setName("PG-13");
        newFilm.setMpa(mpa);

        Genre genre = new Genre();
        genre.setId(3);
        genre.setName("Мультфильм");
        Set<Genre> genres = Set.of(genre);
        newFilm.setGenres(genres);

        filmRepository.add(newFilm);
        assertThat(filmRepository.getAll()).hasSize(3);
    }

    @Test
    public void testUpdateFilm() {
        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("123123");
        updatedFilm.setDescription("1");
        updatedFilm.setReleaseDate(LocalDate.of(2021, 12, 2));
        updatedFilm.setDuration(50);

        Mpa mpa = new Mpa();
        mpa.setId(2);
        mpa.setName("PG");
        updatedFilm.setMpa(mpa);

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");
        Set<Genre> genres = Set.of(genre);
        updatedFilm.setGenres(genres);

        Film returnedFilm = filmRepository.update(updatedFilm);
        assertThat(returnedFilm)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "123123")
                .hasFieldOrPropertyWithValue("description", "1")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2021, 12, 2))
                .hasFieldOrPropertyWithValue("duration", 50)
                .hasFieldOrPropertyWithValue("mpa", mpa)
                .hasFieldOrPropertyWithValue("genres", genre);
    }

    @Test
    public void addLike() {
        Film film = getTestFilm();
        filmRepository.addLike(film.getId(), 3L);

        assertThat(filmRepository.get(1L).get().getLikes()).hasSize(3);
    }

    @Test
    public void removeLike() {
        Film film = getTestFilm();
        filmRepository.removeLike(film.getId(), 2L);

        assertThat(filmRepository.get(1L).get().getLikes()).hasSize(1);
    }

}
