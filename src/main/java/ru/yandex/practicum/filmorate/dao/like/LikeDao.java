package ru.yandex.practicum.filmorate.dao.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface LikeDao {
    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> findPopular(int count);

    Set<Integer> findLikeByFilmId(int filmId);
}
