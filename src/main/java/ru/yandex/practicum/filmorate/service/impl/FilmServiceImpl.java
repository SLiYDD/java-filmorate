package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.service.MpaService;


import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final LikeService likeService;

    private final GenreService genreService;
    private final GenreDao genreDao;
    private final MpaService mpaService;

    @Override
    public Film saveFilm(Film film) {
        setMpaAndGenre(film);
        film.setLikes(new HashSet<>());
        Film newFilm = filmDao.addFilm(film);
        if (Objects.nonNull(film.getGenres())) {
            genreService.insertGenres(newFilm.getId(), new ArrayList<>(film.getGenres()));
        }
        return newFilm;
    }


    @Override
    public Film updateFilm(Film film) {
        setMpaAndGenre(film);
        film.setLikes(likeService.findLikeByFilmId(film.getId()));
        if (filmDao.filmExist(film.getId())) {
            Film updateFilm = filmDao.updateFilm(film);
            if (Objects.nonNull(film.getGenres())) {
                genreService.insertGenres(updateFilm.getId(), new ArrayList<>(film.getGenres()));
            }
            return updateFilm;
        }
        throw new NotFoundException("Фильм с id : " + film.getId() + " не найден");
    }

    private void setMpaAndGenre(Film film) {
        if (film.getMpa() != null) {
            Mpa mpa = film.getMpa();
            if (mpa == null) {
                throw new NotFoundException("Рейтинг с id=" + film.getMpa().getId() + " не найден");
            }
            film.setMpa(mpaService.findById(mpa.getId()));
        }
        Set<Genre> filmGenres = film.getGenres();
        if (Objects.isNull(filmGenres)) {
            return;
        }
        if (!filmGenres.isEmpty()) {
            List<Integer> ids = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());

            Set<Genre> genres = new HashSet<>(genreDao.findByIds(ids));
            if (genres.size() != ids.size()) {
                throw new NotFoundException("Найдены не существующие жанры");
            }
            film.setGenres(genres);
        }
    }

    @Override
    public Film findFilmById(int id) {
        Optional<Film> filmOpt = filmDao.findFilmById(id);
        if (filmOpt.isEmpty()) {
            throw new NotFoundException("Фильм с id : " + id + " не найден");
        }
        var film = filmOpt.get();
        return film.toBuilder()
                .mpa(mpaService.findById(film.getMpa().getId()))
                .likes(new HashSet<>(likeService.findLikeByFilmId(film.getId())))
                .genres(genreService.findGenreByFilm(film.getId()))
                .build();
    }


    @Override
    public List<Film> getAllFilm() {
        List<Film> allFilms = filmDao.findAllFilms();
        if (allFilms.isEmpty()) {
            return allFilms;
        }
        return genreService.loadGenreByIds(allFilms);
    }


    @Override
    public void deleteFilm(int filmId) {
        if (filmDao.filmExist(filmId)) {
            throw new NotFoundException("Фильм с id : " + filmId + " не найден");
        }
        filmDao.deleteFilm(filmId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = existsFilmAndUser(filmId, userId);
        likeService.addLike(filmId, userId);
    }


    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = existsFilmAndUser(filmId, userId);
        likeService.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> findPopular(int count) {
        List<Film> popular = likeService.findPopular(count);
        if (popular.isEmpty()) {
            return genreService.loadGenreByIds(filmDao.findAllFilms()).stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
        return likeService.findPopular(count);
    }

    private Film existsFilmAndUser(int filmId, int userId) {
        Film film = filmDao.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id : " + filmId + " не найден"));

        User user = userDao.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id : " + userId + " не найден"));
        return film;
    }
}
