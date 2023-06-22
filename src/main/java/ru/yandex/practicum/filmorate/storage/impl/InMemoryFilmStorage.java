package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private static Integer globalId = 1;

    @Override
    public Optional<Film> addFilm(Film film) {
        if (films.containsValue(film)) {
            return Optional.empty();
        }
        var filmBuild = film.toBuilder()
                .likes(new HashSet<>())
                .id(generateId())
                .build();
        films.put(filmBuild.getId(), filmBuild);
        return Optional.of(films.get(filmBuild.getId()));
    }

    private int generateId() {
        return globalId++;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        var filmDb = films.get(film.getId());
        if (filmDb != null) {
            BeanUtils.copyProperties(film, filmDb, "id", "likes");
            return Optional.of(filmDb);
        }
        return Optional.empty();
    }

    @Override
    public List<Film> findAllFilms() {
        if (films.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findFilmById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public boolean deleteFilm(int id) {
        return Optional.ofNullable(films.remove(id)).isPresent();
    }
}
