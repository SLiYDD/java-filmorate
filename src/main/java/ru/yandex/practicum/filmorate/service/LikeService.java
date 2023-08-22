package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface LikeService {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Set<Integer> findLikeByFilmId(int filmId);

    List<Film> findPopular(int count);
}
