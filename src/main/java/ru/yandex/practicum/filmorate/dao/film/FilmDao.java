package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAllFilms();

    Optional<Film> findFilmById(Integer id);

    void deleteFilm(int id);

    boolean filmExist(int filmId);
}
