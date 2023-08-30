package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

public interface GenreDao {
    List<Genre> findAll();

    List<Genre> findGenreByFilmId(int filmId);

    Optional<Genre> findById(int id);

    List<Genre> findByIds(List<Integer> genreIds);

    void deleteAllByFilmId(int filmId);

    Optional<Genre> create(Genre genre);

    Optional<Genre> update(Genre genre);

    void insertGenresByFilmId(int filmId, List<Integer> genres);

    List<Film> loadGenre(List<Film> films);
}
