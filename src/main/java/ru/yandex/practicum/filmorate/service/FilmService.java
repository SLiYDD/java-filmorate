package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Film findFilmById(int id);

    List<Film> getAllFilm();

    void deleteFilm(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> findPopular(int count);
}
