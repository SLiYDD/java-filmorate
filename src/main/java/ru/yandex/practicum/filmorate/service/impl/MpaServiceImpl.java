package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDao mpaDao;

    @Override
    public Mpa findById(int mpaId) {
        return mpaDao.findMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + mpaId + " не найден!"));
    }

    @Override
    public List<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }
}
