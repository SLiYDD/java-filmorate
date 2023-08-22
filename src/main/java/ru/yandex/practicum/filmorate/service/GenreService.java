package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    Genre findGenreById(int genreId);

    void insertGenres(int filmId, List<Genre> genres);

    List<Genre> findAllGenres();

    Set<Genre> findGenreByFilm(int filmId);

    List<Film> loadGenreByIds(List<Film> ids);
}
