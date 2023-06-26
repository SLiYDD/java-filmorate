package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        return filmService.saveFilm(film);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление фильма");
        return filmService.updateFilm(film);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilm() {
        log.info("Получен запрос на получение всех фильмов");
        return filmService.getAllFilm();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findPopular(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос на получение популярных фильмов");
        return filmService.findPopular(count);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findFilmById(@PathVariable("id") int id) {
        log.info("Получен запрос на получение фильма по ID");
        return filmService.findFilmById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(@PathVariable("id") @NotNull int id) {
        log.info("Получен запрос на удаления пользователя");
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен запрос на добавление лайка");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен запрос на добавление лайка");
        filmService.deleteLike(id, userId);
    }
}
