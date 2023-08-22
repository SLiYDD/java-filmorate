package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeDao likeDao;

    @Override
    public void addLike(int filmId, int userId) {
        likeDao.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        likeDao.deleteLike(filmId, userId);
    }

    @Override
    public Set<Integer> findLikeByFilmId(int filmId) {
        return likeDao.findLikeByFilmId(filmId);
    }

    @Override
    public List<Film> findPopular(int count) {
        return likeDao.findPopular(count);
    }
}
