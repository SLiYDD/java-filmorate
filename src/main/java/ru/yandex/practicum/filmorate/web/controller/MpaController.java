package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa findMpaById(@PathVariable("id") int id) {
        log.info("Получен запрос на получение рейтинга по ID");
        return mpaService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> findAllMpa() {
        log.info("Получен запрос на получение всех рейтингов");
        return mpaService.findAllMpa();
    }
}
