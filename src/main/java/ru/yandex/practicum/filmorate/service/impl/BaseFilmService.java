package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ResourceAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film saveFilm(Film film) {
        return filmStorage.addFilm(film)
                .orElseThrow(() -> new ResourceAlreadyExistException("Такой фильм уже существует"));
    }


    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film)
                .orElseThrow(() -> new NotFoundException("Фильм с id : " + film.getId() + " не найден"));
    }

    @Override
    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id : " + id + " не найден"));
    }


    @Override
    public List<Film> getAllFilm() {
        return filmStorage.findAllFilms();
    }


    @Override
    public void deleteFilm(int id) {
        if (filmStorage.deleteFilm(id)) {
            return;
        }
        throw new NotFoundException("Фильм с id : " + id + " не найден");
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = foundFilmAndUser(filmId, userId);
        film.getLikes().add(userId);
    }


    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = foundFilmAndUser(filmId, userId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> findPopular(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film foundFilmAndUser(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id : " + filmId + " не найден"));

        User user = userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + userId + " не найден"));
        return film;
    }
}
