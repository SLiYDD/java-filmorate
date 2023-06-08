package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmTest {

    private static Validator validator;

    private Film film;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @BeforeEach
    void initFilm() {
        film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(120)
                .build();
    }


    @Test
    void validateNameTest() {
        var filmTest = film.toBuilder()
                .name(" ")
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(filmTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Не может быть пустым",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }


    @Test
    void validateDescriptionTest() {
        var filmTest = film.toBuilder()
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать " + "господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(filmTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Не может быть больше 200 символов в описании",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }


    @Test
    void validateReleaseDateTest() {
        var filmTest = film.toBuilder()
                .releaseDate(LocalDate.of(1890, 3, 25))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(filmTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895 года",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );

    }


    @Test
    void validateDurationTest() {
        var filmTest = film.toBuilder()
                .duration(-10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(filmTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Продолжительность не может быть отрицательной",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );

    }


    @Test
    void validateCorrectFilmDataTest() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertEquals(0, violations.size());
    }
}
