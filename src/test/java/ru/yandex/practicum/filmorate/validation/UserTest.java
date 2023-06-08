package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class UserTest {
    private static Validator validator;
    private User user;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @BeforeEach
    void initUser() {
        user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1976, 8, 20))
                .build();
    }


    @Test
    void validateLoginTest() {
        var userTest = user.toBuilder()
                .login(user.getLogin() + " ")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(userTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Логин не может содержать пробелы",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }


    @Test
    void validateEmptyEmailTest() {
        var userTest = user.toBuilder()
                .email(" ")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(userTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Введите валидный email адрес",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }

    @Test
    void validateEmailTest() {
        var userTest = user.toBuilder()
                .email("mailmail.ru")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(userTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Введите валидный email адрес",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }


    @Test
    void validateBirthdayTest() {
        var userTest = user.toBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(userTest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, violations.size()),
                () -> Assertions.assertEquals("Дата рождения не может быть в будущем",
                        violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()).get(0))
        );
    }


    @Test
    void validateCorrectUserDataTest() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertEquals(0, violations.size());
    }
}
