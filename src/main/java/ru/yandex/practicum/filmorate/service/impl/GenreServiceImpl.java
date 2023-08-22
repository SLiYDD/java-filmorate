package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;


    @Override
    public Genre findGenreById(int genreId) {
        var genre = genreDao.findById(genreId);
        if (genre.isEmpty()) {
            throw new NotFoundException("Найдены не существующие жанры");
        }
        return genre.get();
    }


    @Override
    public void insertGenres(int filmId, List<Genre> genres) {
        genreDao.deleteAllByFilmId(filmId);
        if (genres.isEmpty()) {
            genreDao.deleteAllByFilmId(filmId);
            return;
        }
        List<Integer> genresIds = genres.stream().map(Genre::getId).collect(Collectors.toList());
        genreDao.insertGenresByFilmId(filmId, genresIds);
    }


    @Override
    public List<Genre> findAllGenres() {
        return genreDao.findAll();
    }


    @Override
    public Set<Genre> findGenreByFilm(int filmId) {
        return new HashSet<>(genreDao.findGenreByFilmId(filmId));
    }


    @Override
    public List<Film> loadGenreByIds(List<Film> films) {
        return genreDao.loadGenre(films);
    }
}

