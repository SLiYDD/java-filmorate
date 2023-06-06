package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ResourceAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class FilmService {
    private HashMap<Integer, Film> films;
    private int count;

    public FilmService() {
        this.films = new HashMap<>();
        count = 1;
    }

    public Film saveFilm(Film film) {
        if (films.containsValue(film)) {
            for (Film value : films.values()) {
                if (value.equals(film)) return value;
            }
        }
        film.setId(count);
        films.put(count, film);
        count++;
        return film;
    }

    public Film changeFilm(Film film) {
        Optional<Film> filmOpt = Optional.ofNullable(films.get(film.getId()));
        if (filmOpt.isEmpty()) {
            throw new NotFoundException("This Film does not exist");
        }
        Film filmDb = filmOpt.get();
        BeanUtils.copyProperties(film, filmDb, "id");
        return films.put(film.getId(), filmDb);
    }

    public List<Film> getAllFilm() {
        if (films.isEmpty()) {
            throw new NotFoundException("The list of films is empty");
        }
        return new ArrayList<>(films.values());
    }
}
