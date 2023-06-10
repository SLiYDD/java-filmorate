package ru.yandex.practicum.filmorate.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ResourceAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;


    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping
    public Film addFilm(@Valid @RequestBody @NotNull Film film) {
        log.info("Получен запрос на добавление фильма");
        try {
            return filmService.saveFilm(film);
        } catch (ResourceAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping
    public Film updateFilm(@Valid @RequestBody @NotNull Film film) {
        log.info("Получен запрос на обновление фильма");
        return filmService.changeFilm(film);
    }


    @GetMapping
    public List<Film> getAllFilm() {
        log.info("Получен запрос на получение всех фильмов");
        return filmService.getAllFilm();
    }
}
